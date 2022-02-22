package com.minimalism.files.service.output;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.minimalism.shared.domain.IngestServiceSummary;
import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.shared.service.FileSystemConfigHelper;

public class IntakeStatsPublisher {
    public void saveStats(String clientName, String inputFileName, String recordName, IngestServiceSummary summary) throws NoSuchPathException, IOException {
        var instrumentationPath = FileSystemConfigHelper.getInstance().getServiceInstrumentationDirectory(clientName);
        String instrFileName = inputFileName.concat("_")
                                .concat(recordName)
                                .concat("_")
                                .concat(LocalDate.now().toString())
                                .concat(".json");
        
                                ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(instrumentationPath.resolve(instrFileName).toString()), summary.toString());
    }
}
