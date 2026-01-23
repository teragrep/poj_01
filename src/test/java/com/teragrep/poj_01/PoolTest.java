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
package com.teragrep.poj_01;

import com.teragrep.poj_01.pool.Pool;
import com.teragrep.poj_01.pool.Poolable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class PoolTest {

    @Test
    void testEmptyPool() {
        final Pool<Poolable> pool = new Pool<>(PoolableFake::new, new PoolableStub());

        final Poolable poolable = pool.get();
        pool.offer(poolable);
        final Poolable poolable2 = pool.get();

        // We should get the same Poolable back from the pool
        Assertions.assertEquals(poolable, poolable2);

        // We should get a new Poolable from the pool as the pool is empty (get() without offer())
        final Poolable poolable3 = pool.get();
        Assertions.assertNotEquals(poolable, poolable3);
        Assertions.assertNotEquals(poolable2, poolable3);

        Assertions.assertFalse(poolable.isStub());
        Assertions.assertFalse(poolable2.isStub());
        Assertions.assertFalse(poolable3.isStub());
    }

    @Test
    void testClosedPool() {
        final Pool<Poolable> pool = new Pool<>(PoolableFake::new, new PoolableStub());
        pool.close();

        final Poolable poolable = pool.get();
        Assertions.assertTrue(poolable.isStub());
    }
}
