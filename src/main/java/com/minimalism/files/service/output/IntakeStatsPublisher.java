package com.minimalism.files.service.output;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonWriter;

import com.minimalism.shared.domain.IngestServiceSummary;
import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.shared.service.FileSystemConfigHelper;

public class IntakeStatsPublisher {
    public void saveStats(String clientName, String inputFileName, String recordName, IngestServiceSummary summary) throws NoSuchPathException, IOException, URISyntaxException {
        var instrumentationPath = FileSystemConfigHelper.getInstance().getServiceInstrumentationDirectory(clientName);
        String instrFileName = inputFileName.concat("_")
                                .concat(recordName)
                                .concat("_")
                                .concat(LocalDate.now().toString())
                                .concat(".json");
        Writer writer = new FileWriter(instrumentationPath.resolve(instrFileName).toString());
        try(JsonWriter summaryWriter = Json.createWriter(writer)) {
            summaryWriter.writeObject(summary.asJson());
        }
    }
}
