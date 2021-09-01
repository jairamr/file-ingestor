package com.minimalism.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;

import com.minimalism.shared.common.AllEnums.Directories;
import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.files.domain.InputOutputFileSystem;

import org.junit.jupiter.api.Test;

public class InputOutputFileSystemTests {
    @Test
    void testCreateFileSystem() {
        try {
            InputOutputFileSystem.createFileSystem("Client_1");
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1", InputOutputFileSystem.getPathFor("Client_1").toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    void testCreateFileSystem_client_name_has_spaces() {
        try {
            InputOutputFileSystem.createFileSystem("Client Name With Spaces");
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_Name_With_Spaces", InputOutputFileSystem.getPathFor("Client Name With Spaces").toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1", InputOutputFileSystem.getPathFor("Client_1").toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_input() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\input", InputOutputFileSystem.getPathFor("Client_1", Directories.INPUT_DATA).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_input_data_csv() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\input\\csv", InputOutputFileSystem.getPathFor("Client_1", Directories.INPUT_DATA_CSV).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_input_data_bin() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\input\\bin", InputOutputFileSystem.getPathFor("Client_1", Directories.INPUT_DATA_BIN).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_input_data_definition() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\input\\definition", InputOutputFileSystem.getPathFor("Client_1", Directories.INPUT_DATA_DEFINITION).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_input_instrumentation() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\input\\instrumentation", InputOutputFileSystem.getPathFor("Client_1", Directories.INSTRUMENTATION).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_output() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\output", InputOutputFileSystem.getPathFor("Client_1", Directories.OUTPUT_DATA).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_output_data_csv() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\output\\csv", InputOutputFileSystem.getPathFor("Client_1", Directories.OUTPUT_DATA_CSV).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_output_data_bin() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\output\\bin", InputOutputFileSystem.getPathFor("Client_1", Directories.OUTPUT_DATA_BIN).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_output_data_definition() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\output\\definition", InputOutputFileSystem.getPathFor("Client_1", Directories.OUTPUT_DATA_DEFINITION).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetPathFor_client_archive() {
        try {
            assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\Client_1\\archive", InputOutputFileSystem.getPathFor("Client_1", Directories.ARCHIVE).toString());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (NoSuchPathException e) {
            System.out.println(e.getMessage());
        }
    }
}
