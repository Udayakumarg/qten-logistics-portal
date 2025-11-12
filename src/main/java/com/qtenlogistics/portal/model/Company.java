package com.qtenlogistics.portal.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Company Entity
 * ---------------------
 * Represents both Shippers and Buyers.
 * Can be grouped by "groupName" to represent business families.
 */

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @Column(nullable = false)
    private String role; // SHIPPER or BUYER

    private String groupName;
    private String customerCode;
    private Boolean active = true;
}
