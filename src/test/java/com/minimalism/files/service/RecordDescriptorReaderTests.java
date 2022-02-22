package com.minimalism.files.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.minimalism.shared.common.AllEnums.DataTypes;
import com.minimalism.shared.common.AllEnums.RecordTypes;
import com.minimalism.files.domain.input.FieldDescriptor;
import com.minimalism.files.domain.input.RecordDescriptor;
import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.files.service.input.RecordDescriptorReader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RecordDescriptorReaderTests {

    private static RecordDescriptor hrRecordDescriptor;

    @BeforeAll
    static void parseDefinitionFile() throws RecordDescriptorException {
        hrRecordDescriptor = RecordDescriptorReader.readDefinition("Client_1", "_HrData_Kaggle_Hr5t.csv");
    }

    @Test
    void testPrepareInput_fileName_starts_with_underscore() {
        assertNotNull(hrRecordDescriptor);
    }

    @Test
    void testPrepareInput_record_descriptor_values_ok() {
        assertEquals(RecordTypes.VARIABLE_LENGTH, hrRecordDescriptor.getRecordType());
        assertEquals(',', hrRecordDescriptor.getFieldSeperator());
        byte[] crlf = {'\r', '\n'};
        assertArrayEquals(crlf, hrRecordDescriptor.getRecordSeparator(), "Sorry, they are not same");
    }

    @Test
    void testPrepareInput_field_descriptor_employee_id_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 0).findFirst().orElse(null);
        assertEquals("empId", iut.getFieldName());
        assertEquals(0, iut.getPosition());
        assertEquals(DataTypes.INTEGER, iut.getDatatype());
        assertEquals(5, iut.getMinimumLength());
        assertEquals(10, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_name_prefix_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 1).findFirst().orElse(null);
        assertEquals("namePrefix", iut.getFieldName());
        assertEquals(1, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(2, iut.getMinimumLength());
        assertEquals(5, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_first_name_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 2).findFirst().orElse(null);
        assertEquals("firstName", iut.getFieldName());
        assertEquals(2, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(1, iut.getMinimumLength());
        assertEquals(30, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_middle_initial_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 3).findFirst().orElse(null);
        assertEquals("middleInitial", iut.getFieldName());
        assertEquals(3, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(1, iut.getMinimumLength());
        assertEquals(4, iut.getMaximumLength());
        assertEquals(true, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_last_name_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 4).findFirst().orElse(null);
        assertEquals("lastName", iut.getFieldName());
        assertEquals(4, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(1, iut.getMinimumLength());
        assertEquals(30, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_gender_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 5).findFirst().orElse(null);
        assertEquals("gender", iut.getFieldName());
        assertEquals(5, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(1, iut.getMinimumLength());
        assertEquals(5, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_father_name_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 7).findFirst().orElse(null);
        assertEquals("fathersName", iut.getFieldName());
        assertEquals(7, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(1, iut.getMinimumLength());
        assertEquals(30, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_mohter_name_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 8).findFirst().orElse(null);
        assertEquals("mothersName", iut.getFieldName());
        assertEquals(8, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(1, iut.getMinimumLength());
        assertEquals(30, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_mohter_maiden_name_prefix_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 9).findFirst().orElse(null);
        assertEquals("mothersMaidenName", iut.getFieldName());
        assertEquals(9, iut.getPosition());
        assertEquals(DataTypes.STRING, iut.getDatatype());
        assertEquals(1, iut.getMinimumLength());
        assertEquals(30, iut.getMaximumLength());
        assertEquals(true, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_date_of_birth_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 10).findFirst().orElse(null);
        assertEquals("dateOfBirth", iut.getFieldName());
        assertEquals(10, iut.getPosition());
        assertEquals(DataTypes.LOCAL_DATE, iut.getDatatype());
        assertEquals(8, iut.getMinimumLength());
        assertEquals(15, iut.getMaximumLength());
        assertEquals(false, iut.isNullAllowed());
    }

    @Test
    void testPrepareInput_field_descriptor_time_of_birth_ok() {
        FieldDescriptor iut = hrRecordDescriptor.getFieldDescriptors().stream().filter(fd -> fd.getPosition() == 11).findFirst().orElse(null);
        assertEquals("timeOfBirth", iut.getFieldName());
        assertEquals(11, iut.getPosition());
        assertEquals(DataTypes.LOCAL_TIME, iut.getDatatype());
        assertEquals(10, iut.getMinimumLength());
        assertEquals(12, iut.getMaximumLength());
        assertEquals(true, iut.isNullAllowed());
    }
}
