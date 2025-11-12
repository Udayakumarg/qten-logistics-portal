package com.qtenlogistics.portal.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer serialNo;
    private String consigneeName;
    private String notify;
    private LocalDate stuffingDate;
    private String shippingLine;
    private String containerNumber;
    private String containerSize;
    private String shipperName;
    private String shipperInvoiceNumber;
    private String hblNumber;
    private Integer totalCartons;
    private Double cbm;
    private Double grossWeight;

    // Movement details
    private String etdOriginPort;
    private String feederVesselVoyage;
    private String etdTransshipmentPort;
    private String motherVesselVoyage;
    private String etaDestinationPort;
    private String destinationPort;

    private String remarks;
}
