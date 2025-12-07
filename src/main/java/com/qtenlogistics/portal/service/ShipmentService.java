package com.qtenlogistics.portal.service;

import com.qtenlogistics.portal.model.Shipment;
import com.qtenlogistics.portal.repository.ShipmentRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    // --- Basic CRUD used elsewhere ------------------------------------------------

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + id));
    }

    public Shipment saveShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(Long id, Shipment updated) {
        Optional<Shipment> existing = shipmentRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Shipment not found: " + id);
        }
        Shipment s = existing.get();
        s.setRemarks(updated.getRemarks());
        s.setEtaDestinationPort(updated.getEtaDestinationPort());
        s.setDestinationPort(updated.getDestinationPort());
        s.setGrossWeight(updated.getGrossWeight());
        s.setTotalCartons(updated.getTotalCartons());
        s.setCbm(updated.getCbm());
        s.setShippingLine(updated.getShippingLine());
        s.setShipperName(updated.getShipperName());
        return shipmentRepository.save(s);
    }

    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }

    // --- Pagination & sorting helpers --------------------------------------------

    /**
     * Paginated fetch (no search filter).
     */
    public Page<Shipment> getPaginatedShipments(int page, int size, String sortBy, String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return shipmentRepository.findAll(pageable);
    }

    /**
     * Paginated + filtered search.
     * If search is blank -> uses repository.findAll(pageable)
     * If search provided -> uses repository.searchShipments(searchLower) returning List,
     * then creates a PageImpl subset for requested page.
     *
     * Note: repository.searchShipments(...) should perform case-insensitive matching
     * (we call toLowerCase() here to be defensive).
     */
    public Page<Shipment> getPaginatedAndFilteredShipments(String search, int page, int size, String sortBy, String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (search == null || search.trim().isEmpty()) {
            // no filter, return pageable repository result
            return shipmentRepository.findAll(pageable);
        }

        // perform repository search (returns List)
        String searchLower = search.trim().toLowerCase(Locale.ROOT);
        List<Shipment> filtered = shipmentRepository.searchShipments(searchLower);
        if (filtered == null || filtered.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // Sort the filtered list according to requested sort (in-memory)
        // Basic approach: create a Sort.Order and apply via Java comparator for common fields.
        // For now rely on insertion order from repository; if you need DB-level sorting for filters,
        // add a native query that supports pageable & filtering.
        // We'll still provide paging wrapper:
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), pageable, filtered.size());
        }
        List<Shipment> pageList = filtered.subList(start, end);
        return new PageImpl<>(pageList, pageable, filtered.size());
    }

    /**
     * Legacy search method returning List (used by API earlier).
     */
    public List<Shipment> searchShipments(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return shipmentRepository.findAll();
        }
        return shipmentRepository.searchShipments(searchText.toLowerCase(Locale.ROOT));
    }
}
