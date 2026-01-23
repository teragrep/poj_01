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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

final class CountingPoolableFake implements CountingPoolable {

    private final AtomicLong report;
    private final List<Integer> counterList;

    CountingPoolableFake(final AtomicLong report) {
        this.report = report;
        this.counterList = new ArrayList<>(1);
        int counter = 0;
        this.counterList.add(counter);
    }

    @Override
    public void increment() {
        // unsynchronized list access here to test concurrent modification
        int counter = counterList.remove(0);
        counter = counter + 1;
        counterList.add(counter);
    }

    @Override
    public boolean isStub() {
        return false;
    }

    @Override
    public void close() throws IOException {
        int counter = counterList.get(0);
        report.addAndGet(counter);
    }
}
