package com.qtenlogistics.portal.repository;

import com.qtenlogistics.portal.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByHblNumberContainingIgnoreCaseOrContainerNumberContainingIgnoreCaseOrShipperNameContainingIgnoreCase(
            String hbl, String container, String shipper);
}
