package com.minimalism.files;

import com.minimalism.files.domain.InputFileInformation;
import com.minimalism.files.service.input.Slicer;

import org.junit.jupiter.api.Test;

public class SlicerTests {
    @Test
    void testSliceFile() {
        Slicer iut = new Slicer(new InputFileInformation());
    }
}
