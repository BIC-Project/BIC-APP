package com.bic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "RECEIPT_TBL", uniqueConstraints = @UniqueConstraint(columnNames = {
		"receiptType", "receiptNo"}))
public class Receipt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int receiptId;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('ER', 'DR')", nullable = false)
	private ReceiptType receiptType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dateTime;

	@Column(nullable = false)
	private String receiptNo;

	@ManyToOne
	@JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
	private Customer customer;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String vehicleNo;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String deliveryPersonName;

	@Column(columnDefinition = "varchar(10)", nullable = false)
	private String deliveryPersonContact;

	@Column(columnDefinition = "varchar(500)", nullable = false)
	private String allCylinders;

	@Column(nullable = false)
	private boolean receiptStatus;
}
