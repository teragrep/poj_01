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

import java.util.function.Supplier;

public interface Pool<T extends Poolable> extends AutoCloseable, Supplier<T> {

    public abstract T get();

    public abstract void offer(T object);

    public abstract void close();
}
