package br.com.elibrary.util.log;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Log {
    private final String action;
    private final LocalDateTime occurredAt;
    private long timeTaken;
    private Map<String, Object> additionalProperties;

    public Log(String action) {
        this.action = action;
        this.occurredAt = LocalDateTime.now();
    }

    public void timeTaken(long timeInNanoSeconds) {
        this.timeTaken = timeInNanoSeconds / 1_000_000;
    }

    public void addProperty(String key, Object value) {
        if (this.additionalProperties == null) {
            this.additionalProperties = new HashMap<>();
        }
        this.additionalProperties.put(key, value);
    }

    @Override
    public String toString() {
        return "Log{" +
                "action='" + action + '\'' +
                ", occurredAt=" + occurredAt +
                ", timeTaken=" + timeTaken +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
