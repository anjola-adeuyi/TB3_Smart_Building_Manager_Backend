package com.emse.spring.automacorp.model;

import com.opencsv.bean.CsvBindByName;

public class MeasurementCsvRepresentation {
    @CsvBindByName(column = "Time")
    private String time;
    @CsvBindByName(column = "Temperature")
    private String temperature;

    public MeasurementCsvRepresentation() {
    }

    @Override
    public String toString() {
        return "MeasurementCsvRepresentation{" +
                "time='" + time + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
