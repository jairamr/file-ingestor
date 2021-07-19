package com.minimalism.common;

public class AllEnums {
    public enum FileTypes{
        BIN,
        CSV,
        TEXT
    }

    public enum RecordTypes {
        DELIMITED,
        FIXED_LENGTH,
        VARIABLE_LENGTH
    }

    public enum DataTypes {
        BOOLEAN,
        CURRENCY,
        DATE,
        EMAIL,
        INTEGER,
        DOUBLE,
        STRING,
        TIME
    }

    public enum Directories {
        INPUT_DATA,
        INPUT_DATA_CSV,
        INPUT_DATA_BIN,
        INPUT_DATA_DEFINITION,
        INSTRUMENTATION,
        OUTPUT_DATA,
        OUTPUT_DATA_CSV,
        OUTPUT_DATA_BIN,
        OUTPUT_DATA_DEFINITION,
        LOST_AND_FOUND,
        ARCHIVE,
        ARCHIVE_INPUT,
        ARCHIVE_INPUT_CSV,
        ARCHIVE_INPUT_BIN,
        ARCHIVE_OUTPUT,
        ARCHIVE_OUTPUT_CSV,
        ARCHIVE_OUTPUT_BIN
    }
}
