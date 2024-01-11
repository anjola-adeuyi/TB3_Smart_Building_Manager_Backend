package com.emse.spring.automacorp.model.records.dto;

import com.emse.spring.automacorp.model.WindowStatus;

public record WindowCommand(String name, WindowStatus windowStatus, Long roomId) {
}
