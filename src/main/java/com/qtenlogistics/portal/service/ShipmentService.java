package com.qtenlogistics.portal.service;

import com.qtenlogistics.portal.model.Shipment;
import com.qtenlogistics.portal.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ShipmentService (AVR Feed-aligned)
 * ----------------------------------
 * Handles business logic for reading, searching, and saving shipments.
 * Works with the new Shipment entity fields.
 */

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    /**
     * Returns all shipments if no search term is provided.
     * If search term is provided, filters by HBL Number, Container Number, or Shipper Name.
     */
    public List<Shipment> getFilteredShipments(String search) {
        if (search == null || search.trim().isEmpty()) {
            return shipmentRepository.findAll();
        }

        return shipmentRepository.findByHblNumberContainingIgnoreCaseOrContainerNumberContainingIgnoreCaseOrShipperNameContainingIgnoreCase(
                search.trim(), search.trim(), search.trim());
    }

    /**
     * Returns all shipments (unfiltered).
     */
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    /**
     * Saves or updates a shipment record.
     */
    public Shipment saveShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    /**
     * Saves multiple shipments in batch (used for CSV import).
     */
    public List<Shipment> saveAllShipments(List<Shipment> shipments) {
        return shipmentRepository.saveAll(shipments);
    }
}
