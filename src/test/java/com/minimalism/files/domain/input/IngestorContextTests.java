package com.minimalism.files.domain.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.shared.common.AllEnums.DataSources;
import com.minimalism.shared.common.AllEnums.FileTypes;
import com.minimalism.shared.exceptions.FileTypeNotSupportedException;
import com.minimalism.shared.exceptions.InvalidFileException;
import com.minimalism.shared.exceptions.NoSuchPathException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IngestorContextTests {
    private static IngestorContext iut;
    @BeforeAll
    static void init() {
        try {
            iut = new IngestorContext("client_1", "_HrData_Kaggle_Hr10t.csv");
        } catch (IOException | FileTypeNotSupportedException | InvalidFileException | NoSuchPathException
                | RecordDescriptorException e) {
            e.printStackTrace();
        }
    }
    @Test
    void testGetAvroSchema_schema_is_generated() {
        assertNotNull(iut.getAvroSchema());
    }

    @Test
    void testGetClientName() {
        assertEquals("client_1", iut.getClientName());
    }

    @Test
    void testGetDestinationType() {
        assertEquals(DataSources.KAFKA, iut.getDestinationType());
    }

    @Test
    void testGetFailoverDestinationType() {
        assertEquals(DataSources.NTFS, iut.getFailoverDestinationType());
    }

    @Test
    void testGetInputFileInformation() {
        InputFileInformation fileInfo = iut.getInputFileInformation();
        System.out.println(fileInfo.getDirectory());
        assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\clients\\client_1\\input\\csv", fileInfo.getDirectory().toString());
        assertEquals("csv", fileInfo.getFileExtension());
        assertEquals("_HrData_Kaggle_Hr10t", fileInfo.getFileName());
        assertEquals("C:\\Users\\jaira\\FileSliceAndDice\\clients\\client_1\\input\\csv\\_HrData_Kaggle_Hr10t.csv", fileInfo.getFilePath().toString());
        assertEquals(2788098, fileInfo.getFileSize());
        assertEquals(FileTypes.CSV, fileInfo.getFileType());
        //assertEquals(FileTime.from(1629217388, TimeUnit.SECONDS), fileInfo.getFileTimestamp());
        

    }

    @Test
    void testGetOperatingMode() {
        assertEquals("balanced", iut.getOperatingMode());
    }

    @Test
    void testGetRecordDescriptor() {
        RecordDescriptor rd = iut.getRecordDescriptor();
        assertEquals("Entity", rd.getEntityClassName());
        assertEquals(",", rd.getFieldSeparatorAsString());
        assertEquals(37, rd.getFieldDescriptors().size());
        assertEquals("HrData", rd.getRecordName());
        assertEquals(147, rd.getMinRecordSize());
        byte[] crlf = new byte[] {'\r','\n'};
        assertEquals(crlf[0], rd.getRecordSeparator()[0]);
        assertEquals(crlf[1], rd.getRecordSeparator()[1]);
        
    }

    @Test
    void testGetRecordName() {

    }
}
