package com.qtenlogistics.portal.service;

import com.qtenlogistics.portal.model.Shipment;
import com.qtenlogistics.portal.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ShipmentFeedService
 * ----------------------
 * Imports shipment data from a CSV file (like AVR Shipments CSV)
 * and saves it into the shipments table in PostgreSQL.
 */

@Service
@RequiredArgsConstructor
public class ShipmentFeedService {


    private final ShipmentRepository shipmentRepository;

    public void importFromCsv(MultipartFile file) {
        List<Shipment> shipments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine(); // Skip header
            if (headerLine == null) {
                throw new RuntimeException("CSV file is empty!");
            }

            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Split CSV by comma (your data is simple, no quoted commas)
                String[] cols = line.split(",", -1);

                Shipment shipment = Shipment.builder()
                        .serialNo(parseInt(cols[0]))
                        .consigneeName(cols[1].trim())
                        .notify(cols[2].trim())
                        .stuffingDate(parseDate(cols[3], formatter))
                        .shippingLine(cols[4].trim())
                        .containerNumber(cols[5].trim())
                        .containerSize(cols[6].trim())
                        .shipperName(cols[7].trim())
                        .shipperInvoiceNumber(cols[8].trim())
                        .hblNumber(cols[9].trim())
                        .totalCartons(parseInt(cols[10]))
                        .cbm(parseDouble(cols[11]))
                        .grossWeight(parseDouble(cols[12]))
                        .etdOriginPort(cols[13].trim())          // ETD Tuticorin
                        .feederVesselVoyage(cols[14].trim())     // Feeder Vsl / Voy
                        .etdTransshipmentPort(cols[15].trim())   // ETD Colombo
                        .motherVesselVoyage(cols[16].trim())     // Mother Vsl / Voy
                        .etaDestinationPort(cols[17].trim())     // ETA New York → ETA Destination Port
                        .destinationPort(extractDestination(cols[17])) // Derived (e.g., “New York”)
                        .remarks(cols.length > 18 ? cols[18].trim() : "")
                        .build();

                shipments.add(shipment);
            }

            shipmentRepository.saveAll(shipments);

        } catch (Exception e) {
            throw new RuntimeException("Failed to import CSV: " + e.getMessage(), e);
        }
    }

    private Integer parseInt(String s) {
        try {
            return (s == null || s.isEmpty()) ? null : (int) Double.parseDouble(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseDouble(String s) {
        try {
            return (s == null || s.isEmpty()) ? null : Double.parseDouble(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate parseDate(String s, DateTimeFormatter formatter) {
        try {
            return (s == null || s.isEmpty()) ? null : LocalDate.parse(s.trim(), formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractDestination(String etaField) {
        // ETA field often reads like “ETA New York” — take the last word
        if (etaField == null || etaField.isEmpty()) return null;
        String[] parts = etaField.trim().split(" ");
        return parts.length > 1 ? parts[1] : parts[0];
    }
}
