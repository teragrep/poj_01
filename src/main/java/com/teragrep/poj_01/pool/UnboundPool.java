/*
 * Teragrep Pooling Library for Java
 * Copyright (C) 2026 Suomen Kanuuna Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teragrep.poj_01.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnboundPool<T extends Poolable> implements Pool<T> {

    private final PoolableSupplier<Pool<T>, T> poolableSupplier;

    private final ConcurrentLinkedQueue<T> queue;

    private final T stub;

    private final Lock lock = new ReentrantLock();

    private final AtomicBoolean close;

    public UnboundPool(final PoolableSupplier<Pool<T>, T> poolableSupplier, T stub) {
        this.poolableSupplier = poolableSupplier;
        this.queue = new ConcurrentLinkedQueue<>();
        this.stub = stub;
        this.close = new AtomicBoolean();
    }

    @Override
    public T get() {
        T object;
        if (close.get()) {
            object = stub;
        }
        else {
            // get or create
            object = queue.poll();
            if (object == null) {
                object = poolableSupplier.apply(this);
            }
        }

        return object;
    }

    @Override
    public void offer(T object) {
        if (!object.isStub()) {
            queue.add(object);
        }

        if (close.get()) {
            while (queue.peek() != null) {
                if (lock.tryLock()) {
                    while (true) {
                        T pooled = queue.poll();
                        if (pooled == null) {
                            break;
                        }
                        else {
                            poolableSupplier.accept(pooled);
                        }
                    }
                    lock.unlock();
                }
                else {
                    break;
                }
            }
        }
    }

    @Override
    public void close() {
        close.set(true);

        // close all that are in the pool right now
        offer(stub);
    }
}
