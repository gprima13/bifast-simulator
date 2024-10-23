package com.alto.id.bifast.simulator.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestCategory getTestCategorySample1() {
        return new TestCategory().id(1L).testSuiteName("testSuiteName1");
    }

    public static TestCategory getTestCategorySample2() {
        return new TestCategory().id(2L).testSuiteName("testSuiteName2");
    }

    public static TestCategory getTestCategoryRandomSampleGenerator() {
        return new TestCategory().id(longCount.incrementAndGet()).testSuiteName(UUID.randomUUID().toString());
    }
}
