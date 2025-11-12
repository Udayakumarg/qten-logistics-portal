package com.qtenlogistics.portal.service;

import com.qtenlogistics.portal.model.Shipment;
import com.qtenlogistics.portal.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    // ✅ New method for backend search
    public List<Shipment> searchShipments(String searchText) {
        return shipmentRepository.searchShipments(searchText.toLowerCase());
    }
}
