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
@Embeddable
public class Stock {

	@EmbeddedId
	private CompositeLocationCustomerCylinder compositeCustomerCylinderId;
	@Column(nullable = false)
	private int cylinderStock;
}

// @IdClass(CompositeLocationCustomerCylinder.class)

// @Id
// @ManyToOne
// @JoinColumn(name = "customerId", referencedColumnName = "customerId",
// nullable = false)
// private Customer customer;
//
// @Id
// @ManyToOne
// @JoinColumn(name = "cylinderId", referencedColumnName = "cylinderId",
// nullable = false)
// private Cylinder cylinder;
