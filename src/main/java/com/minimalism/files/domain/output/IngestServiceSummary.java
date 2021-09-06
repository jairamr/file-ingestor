package com.minimalism.files.domain.output;

import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class IngestServiceSummary {
    private Set<IterationStatistics> iterationsStats;
    
    public IngestServiceSummary() {
        iterationsStats = new HashSet<>();
    }
    public void addStat(IterationStatistics stat) {
        this.iterationsStats.add(stat);
    }
    public Set<IterationStatistics> getStats() {
        return this.iterationsStats;
    }
    public IterationStatistics getStatsFor(int iterationNumber) {
        if(iterationNumber < 0) {
            return null;
        } else if(iterationNumber >= this.numberOfIterations()) {
            return null;
        } else {
            return this.iterationsStats.stream()
                    .filter(is -> is.getIterationNumber() == iterationNumber)
                    .findFirst()
                    .orElse(null);
        }
    }
    public boolean inputFileHadInvalidRecords() {
        return this.iterationsStats.stream()
                .filter(is -> is.getInvalidRecords() > 0)
                .findAny().isEmpty();
    }
    public boolean inputFileHadMissingInformation() {
        return this.iterationsStats.stream()
                .filter(is -> is.getMissingInformationRecords() > 0)
                .findAny().isEmpty();
    }
    public boolean iterationHadNoErrors() {
        return !inputFileHadInvalidRecords();
    }
    public int numberOfIterations() {
        return this.iterationsStats.size();
    }
    @Override
    public String toString() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(IterationStatistics is : this.iterationsStats) {
            arrayBuilder.add(is.asJson());
        }
        return Json.createObjectBuilder()
                .add("ingestServiceSummary", arrayBuilder)
                .build()
                .toString();
    }
}
