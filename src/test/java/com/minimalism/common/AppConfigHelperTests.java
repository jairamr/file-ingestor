package com.minimalism.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import com.minimalism.AppConfigHelper;

import org.junit.jupiter.api.Test;

public class AppConfigHelperTests {
    @Test
    void testGetBufferSize() {
        try {
            assertEquals(1024*1024, AppConfigHelper.getInstance().getBufferSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetInstance() {
        try {
            assertTrue(AppConfigHelper.getInstance() != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetServiceRootDirectory() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\", AppConfigHelper.getInstance().getServiceRootDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetThreadsLoadingFactor() {
        try {
            assertEquals(3, AppConfigHelper.getInstance().getThreadsLoadingFactor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
