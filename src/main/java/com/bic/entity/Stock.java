package com.bic.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "STOCK_TBL")
//@IdClass(CompositeCustomerCylinder.class)
@Embeddable
public class Stock {

//    @Id
//    @ManyToOne
//    @JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
//    private Customer customer;
//
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "cylinderId", referencedColumnName = "cylinderId", nullable = false)
//    private Cylinder cylinder;

    @EmbeddedId
    private CompositeCustomerCylinder CompositeCustomerCylinderId;
    @Column(nullable = false)
    private int cylinderStock;
}
