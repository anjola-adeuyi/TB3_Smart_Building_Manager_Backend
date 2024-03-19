package com.emse.spring.automacorp.model;

import java.time.LocalDateTime;

public class TimeRange {
    private LocalDateTime oldestTime;
    private LocalDateTime earliestTime;

    public TimeRange(LocalDateTime oldestTime, LocalDateTime earliestTime) {
        this.oldestTime = oldestTime;
        this.earliestTime = earliestTime;
    }

    public LocalDateTime getOldestTime() {
        return oldestTime;
    }

    public void setOldestTime(LocalDateTime oldestTime) {
        this.oldestTime = oldestTime;
    }

    public LocalDateTime getEarliestTime() {
        return earliestTime;
    }

    public void setEarliestTime(LocalDateTime earliestTime) {
        this.earliestTime = earliestTime;
    }
}
