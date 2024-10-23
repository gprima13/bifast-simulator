package com.alto.id.bifast.simulator.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestCaseDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestCaseDetail getTestCaseDetailSample1() {
        return new TestCaseDetail().id(1L).matchingParam("matchingParam1").matchingValue("matchingValue1");
    }

    public static TestCaseDetail getTestCaseDetailSample2() {
        return new TestCaseDetail().id(2L).matchingParam("matchingParam2").matchingValue("matchingValue2");
    }

    public static TestCaseDetail getTestCaseDetailRandomSampleGenerator() {
        return new TestCaseDetail()
            .id(longCount.incrementAndGet())
            .matchingParam(UUID.randomUUID().toString())
            .matchingValue(UUID.randomUUID().toString());
    }
}
