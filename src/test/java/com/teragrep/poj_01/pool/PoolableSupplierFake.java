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

import java.util.function.Consumer;
import java.util.function.Function;

final class PoolableSupplierFake<R, T> implements PoolableSupplier<R, T> {

    private final Function<R, T> supplyFunction;
    private final Consumer<T> deallocConsumer;

    PoolableSupplierFake(final Function<R, T> supplyFunction, final Consumer<T> deallocConsumer) {
        this.supplyFunction = supplyFunction;
        this.deallocConsumer = deallocConsumer;
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public void accept(final T poolable) {
        deallocConsumer.accept(poolable);
    }

    @Override
    public T apply(final R r) {
        return supplyFunction.apply(r);
    }
}
