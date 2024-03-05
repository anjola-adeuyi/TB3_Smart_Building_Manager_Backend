package com.emse.spring.automacorp.model;

import jakarta.persistence.*;
import org.hibernate.dialect.function.CommonFunctionFactory;

@Entity
@Table
public class Measurement {
    @Id
    @SequenceGenerator(
            name = "measurement_sequence",
            sequenceName = "measurement_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "measurement_sequence"
    )
    private Integer id;
    private String time;
    private String temperature;

    public Measurement() {
    }

    // Builder pattern
    public static class Builder {
        private Integer id;
        private String time;
        private String temperature;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder time(String time) {
            this.time = time;
            return this;
        }

        public Builder temperature(String temperature) {
            this.temperature = temperature;
            return this;
        }

        public Measurement build() {
            Measurement measurement = new Measurement();
            measurement.setId(this.id);
            measurement.setTime(this.time);
            measurement.setTemperature(this.temperature);
            return measurement;
        }
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
