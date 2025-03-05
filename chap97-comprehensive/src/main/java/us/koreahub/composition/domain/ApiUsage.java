package us.koreahub.domain;

import java.io.Serializable;
import java.util.Date;

public class ApiUsage implements Serializable {
    private final Date timestamp;
    private final String model;
    private final int tokenCount;
    private final double cost;

    public ApiUsage(Date timestamp, String model, int tokenCount, double cost) {
        this.timestamp = timestamp;
        this.model = model;
        this.tokenCount = tokenCount;
        this.cost = cost;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getModel() {
        return model;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    abstract getCost();

    @Override
    public String toString() {
        return String.format("[%s] Model: %s, Tokens: %d, Cost: $%.6f",
                timestamp, model, tokenCount, cost);
    }
}