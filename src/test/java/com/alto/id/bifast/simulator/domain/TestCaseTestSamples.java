package com.alto.id.bifast.simulator.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestCaseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestCase getTestCaseSample1() {
        return new TestCase()
            .id(1L)
            .caseName("caseName1")
            .responseBody("responseBody1")
            .transactionStatus("transactionStatus1")
            .responseCode("responseCode1");
    }

    public static TestCase getTestCaseSample2() {
        return new TestCase()
            .id(2L)
            .caseName("caseName2")
            .responseBody("responseBody2")
            .transactionStatus("transactionStatus2")
            .responseCode("responseCode2");
    }

    public static TestCase getTestCaseRandomSampleGenerator() {
        return new TestCase()
            .id(longCount.incrementAndGet())
            .caseName(UUID.randomUUID().toString())
            .responseBody(UUID.randomUUID().toString())
            .transactionStatus(UUID.randomUUID().toString())
            .responseCode(UUID.randomUUID().toString());
    }
}
