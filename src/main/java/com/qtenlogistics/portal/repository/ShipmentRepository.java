package com.qtenlogistics.portal.repository;

import com.qtenlogistics.portal.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    // ✅ Case-insensitive partial match search across key fields
    @Query("""
        SELECT s FROM Shipment s
        WHERE LOWER(s.consigneeName) LIKE %:text%
           OR LOWER(s.notify) LIKE %:text%
           OR LOWER(s.containerNumber) LIKE %:text%
           OR LOWER(s.shippingLine) LIKE %:text%
           OR LOWER(s.shipperName) LIKE %:text%
           OR LOWER(s.hblNumber) LIKE %:text%
           OR LOWER(s.destinationPort) LIKE %:text%
           OR LOWER(s.remarks) LIKE %:text%
        """)
    List<Shipment> searchShipments(@Param("text") String text);
}
