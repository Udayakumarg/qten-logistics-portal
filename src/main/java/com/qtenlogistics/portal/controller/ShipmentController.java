package com.qtenlogistics.portal.controller;

import com.qtenlogistics.portal.model.Shipment;
import com.qtenlogistics.portal.service.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    /**
     * Renders the full Thymeleaf page. The page's JS will read the URL state and
     * trigger an AJAX load; but rendering this view ensures direct visits work.
     */
    @GetMapping("/shipments/view")
    public String viewShipments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "stuffingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String search,
            Model model
    ) {
        // Provide initial attributes so Thymeleaf renders a consistent shell.
        Page<Shipment> shipmentPage = shipmentService.getPaginatedAndFilteredShipments(search, page, size, sortBy, direction);

        model.addAttribute("shipments", shipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", shipmentPage.getTotalPages());
        model.addAttribute("totalItems", shipmentPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("search", search);

        return "tracking";
    }

    /**
     * AJAX endpoint: returns only the table-body fragment (rows + hidden paging metadata).
     * Called by the front-end JS with X-Requested-With header.
     */
    @GetMapping("/shipments/search")
    public String searchShipmentsAjax(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "stuffingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Model model
    ) {
        Page<Shipment> shipmentPage = shipmentService.getPaginatedAndFilteredShipments(search, page, size, sortBy, direction);

        model.addAttribute("shipments", shipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", shipmentPage.getTotalPages());
        model.addAttribute("totalItems", shipmentPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("search", search);

        // return only the tbody fragment prepared at: templates/fragments/shipment-table.html
        return "fragments/shipment-table :: shipmentTableBody";
    }
}
