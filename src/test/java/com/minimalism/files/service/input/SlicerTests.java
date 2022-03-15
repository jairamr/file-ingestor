package com.minimalism.files.service.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.minimalism.files.domain.SystemRecources;
import com.minimalism.files.domain.input.IngestorContext;
import com.minimalism.files.domain.input.InputFileInformation;
import com.minimalism.files.domain.input.SlicerConfigurationInformation;
import com.minimalism.files.exceptions.ResourceAllocationException;
import com.minimalism.shared.domain.ServiceConfiguration;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlicerTests {

    private Slicer iut;
    InputFileInformation mockInputFileInformation = mock(InputFileInformation.class);
    ServiceConfiguration mockServiceConfiguration = mock(ServiceConfiguration.class);
    @InjectMocks
    IngestorContext mockContext = mock(IngestorContext.class);
     
    
    @Test
    void testSliceFile_gt1gib_8cores() {
        when(mockContext.getInputFileInformation()).thenReturn(mockInputFileInformation);
        when(mockContext.getServiceConfiguration()).thenReturn(mockServiceConfiguration);
        when(mockContext.getInputFileInformation().getFileSize()).thenReturn((long)1357898*1024);
        when(mockContext.getServiceConfiguration().getOperatingMode()).thenReturn("balanced");
        when(mockContext.getServiceConfiguration().getThreadsLoadingFactor()).thenReturn(1);
        
        try {
            iut = new Slicer(mockContext);
        } catch (ResourceAllocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        SlicerConfigurationInformation result = iut.sliceFile();
        assertEquals(497, result.getIterationCount());
    }
}
