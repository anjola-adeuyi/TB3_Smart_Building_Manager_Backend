package com.emse.spring.automacorp.model;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/sensor")
@Transactional
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @GetMapping
    public String getStudents() {
        return "Jola";
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Integer> uploadMeasurements(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(service.uploadMeasurement(file));
    }

    @GetMapping("/measurements")
    public ResponseEntity<List<Measurement>> getAllMeasurements() {
        List<Measurement> measurements = service.getAllMeasurements();
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/time-range")
    public ResponseEntity<TimeRange> getTimeRange() {
        TimeRange timeRange = service.getTimeRange();
        return ResponseEntity.ok(timeRange);
    }
}
