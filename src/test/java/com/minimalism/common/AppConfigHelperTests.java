package com.minimalism.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import com.minimalism.shared.service.AppConfigHelper;

import org.junit.jupiter.api.Test;

public class AppConfigHelperTests {

    @Test
    void testGetInstance() {
        try {
            assertNotNull(AppConfigHelper.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetServiceRootDirectory() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice", AppConfigHelper.getInstance().getServiceRootDirectory().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
