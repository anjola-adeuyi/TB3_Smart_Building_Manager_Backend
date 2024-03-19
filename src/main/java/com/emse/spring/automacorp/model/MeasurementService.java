package com.emse.spring.automacorp.model;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private final MeasurementRepository repository;

    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public Integer uploadMeasurement(MultipartFile file) throws IOException {
        Set<Measurement> measurement = parseCsv(file);
        repository.saveAll(measurement);
        return measurement.size();
    }

    public List<Measurement> getAllMeasurements() {
        return repository.findAll();
    }

    public List<LocalDateTime> getAllMeasurementTimes() {
        List<Measurement> measurements = repository.findAll();
        return measurements.stream()
                .map(Measurement::getParsedTime)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TimeRange getTimeRange() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String oldestTimeStr = repository.findOldestTime();
        String earliestTimeStr = repository.findEarliestTime();
        LocalDateTime oldestTime = LocalDateTime.parse(oldestTimeStr, formatter);
        LocalDateTime earliestTime = LocalDateTime.parse(earliestTimeStr, formatter);
        return new TimeRange(oldestTime, earliestTime);
    }

    private Set<Measurement> parseCsv(MultipartFile file) throws IOException {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<MeasurementCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(MeasurementCsvRepresentation.class);
            CsvToBean<MeasurementCsvRepresentation> csvToBean =
                    new CsvToBeanBuilder<MeasurementCsvRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> new Measurement.Builder()
                            .time(csvLine.getTime())
                            .temperature(csvLine.getTemperature())
                            .build()
                    )

//                            Measurement.builder().
//                            .time(csvLine.getTime())
//                            .temperature(csvLine.getTemperature())
//                            .build()
//                    )
                    .collect(Collectors.toSet());
        }
    }
}
