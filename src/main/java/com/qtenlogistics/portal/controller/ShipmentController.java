package com.qtenlogistics.portal.controller;

import com.qtenlogistics.portal.model.Shipment;
import com.qtenlogistics.portal.service.ShipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    // ✅ Back-end search filter
    @GetMapping("/shipments/view")
    public String viewShipments(@RequestParam(required = false) String search, Model model) {
        List<Shipment> shipments = (search == null || search.isBlank())
                ? shipmentService.getAllShipments()
                : shipmentService.searchShipments(search.trim());

        model.addAttribute("shipments", shipments);
        model.addAttribute("search", search); // to retain search box value
        return "tracking";
    }
}
