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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	@Pattern(regexp = "^[ED]R$", message = "Invalid receipt type selected. A valid receipt type is ER or DR.")
	@NotBlank(message = "Receipt type cannot be blank.")
	private ReceiptType receiptType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotBlank(message = "Receipt creation date cannot be blank.")
	private Date dateTime;

	@Column(nullable = false)
	private String receiptNo;

	@ManyToOne
	@JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
	@NotBlank(message = "Customer cannot be blank.")
	private Customer customer;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	@NotBlank(message = "Vehicle no cannot be blank.")
	private String vehicleNo;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	@NotBlank(message = "Delivery person name cannot be blank.")
	private String deliveryPersonName;

	@Column(columnDefinition = "varchar(10)", nullable = false)
	@NotBlank(message = "Delivery person contact number cannot be empty.")
	@Pattern(regexp = "^[0-9]{3,10}$", message = "Dilevery person contact number should only contain digits. Ex. 91XXXXXXXX")
	@Size(min = 3, max = 10, message = "Contact number should be between 3 and 10 digits.")
	private String deliveryPersonContact;

	@Column(columnDefinition = "varchar(500)", nullable = false)
	@NotBlank(message = "Cylinder list cannot be empty.")
	private String allCylinders;

	@Column(nullable = false)
	private boolean receiptStatus;
}
