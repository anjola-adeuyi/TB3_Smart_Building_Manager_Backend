package com.emse.spring.automacorp.model.records.dto;

import com.emse.spring.automacorp.model.HeaterStatus;

public record HeaterCommand(String name, HeaterStatus heaterStatus, Long roomId) {
}
