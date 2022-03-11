package com.minimalism.common;

public class AllEnums {
    /**
     * Enum indicating possible destinations for the File Ingestor. 
     * @implNote <em> KAFKA Producer is the only way to publish records right now.</em> 
     */
    public enum OutputDestinations {
        AMQP,
        DATABASE,
        FILESYSTEM,
        KAFKA,
        RESTFUL,
        WEBSOCKET
    }

    public enum BufferReaderStatus {
        COMPLETED,
        COMPLETED_WITH_ERRORS,
        COMPLETED_WITH_EXCEPTION,
        INPUT_BUFFER_EMPTY,

    }
}
