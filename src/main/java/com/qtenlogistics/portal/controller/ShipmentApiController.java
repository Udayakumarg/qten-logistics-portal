package com.qtenlogistics.portal.controller;

import com.qtenlogistics.portal.model.Shipment;
import com.qtenlogistics.portal.service.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentApiController {

    private final ShipmentService shipmentService;

    public ShipmentApiController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    // ✅ 1. Paginated endpoint
    @GetMapping
    public Page<Shipment> getShipments(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "stuffingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return shipmentService.getPaginatedAndFilteredShipments(search, page, size, sortBy, direction);
    }

    // ✅ 2. Search
    @GetMapping("/search")
    public List<Shipment> searchShipments(@RequestParam("q") String query) {
        return shipmentService.searchShipments(query);
    }

    // ✅ 3. CRUD operations (same as before)
    @GetMapping("/{id}")
    public Shipment getShipmentById(@PathVariable Long id) {
        return shipmentService.getShipmentById(id);
    }

    @PostMapping
    public Shipment createShipment(@RequestBody Shipment shipment) {
        return shipmentService.saveShipment(shipment);
    }

    @PutMapping("/{id}")
    public Shipment updateShipment(@PathVariable Long id, @RequestBody Shipment updated) {
        return shipmentService.updateShipment(id, updated);
    }

    @DeleteMapping("/{id}")
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }
}
