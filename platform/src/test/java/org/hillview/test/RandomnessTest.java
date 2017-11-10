/*
 * Copyright (c) 2017 VMware Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hillview.test;

import org.apache.commons.math3.random.MersenneTwister;
import org.hillview.utils.Randomness;
import org.junit.Test;
import java.util.Random;


public class RandomnessTest extends BaseTest {
    private final Random randomPRG = new Random();
    private final MersenneTwister MT_PRG = new MersenneTwister();

    @Test
    public void testRandomnessPerf() {
        int iterationNum = 100; // number of iterations
        int length = 100000; // number of random numbers to generate

        TestUtil.runPerfTest((k) -> totalRandom(length), iterationNum);
        TestUtil.runPerfTest((k) -> totalMT(length), iterationNum);
    }

    private void totalRandom(int k) {
        for (int i = 0; i < k; i++)
            this.randomPRG.nextInt();
    }

    private void totalMT(int k) {
        for (int i = 0; i < k; i++)
            this.MT_PRG.nextInt();
    }

    private void totalG(int k, Randomness prg) {
        for (int i = 0; i < k; i++)
            prg.nextGeometric(0.01);
    }

    private void totalGE(int k, Randomness prg) {
        for (int i = 0; i < k; i++)
            prg.nextGeometric(0.01);
    }
    @Test
    public void testGeometric(){
        Randomness prg = new Randomness(123456);
        double sum1 =0, sum2 = 0;
        for (int i = 0; i < 1000; i++) {
            sum1 += prg.nextGeometric(0.02);
            sum2 += prg.nextGeometric(0.02);
        }
        System.out.println("double: " + sum1/1000 + " int: " + sum2/1000);
        TestUtil.runPerfTest((k) -> totalG(100000, prg), 100);
        TestUtil.runPerfTest((k) -> totalGE(100000, prg), 100);
    }

}