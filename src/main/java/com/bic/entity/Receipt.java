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

import org.springframework.format.annotation.DateTimeFormat;

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
	@javax.validation.constraints.NotNull(message = "Receipt type cannot be blank.")
	private ReceiptType receiptType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@javax.validation.constraints.NotNull(message = "Date cannot be blank.")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dateTime;

	@Column(nullable = false)
	private String receiptNo;

	@ManyToOne
	@JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
	@javax.validation.constraints.NotNull(message = "Customer cannot be blank.")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "locationId", referencedColumnName = "locationId", nullable = false)
	@javax.validation.constraints.NotNull(message = "Location cannot be blank.")
	private Location location;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	@NotBlank(message = "Vehicle no. cannot be blank.")
	@Size(min = 3, max = 30, message = "Vehicle no. should be greater that 3 characters.")
	private String vehicleNo;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	@NotBlank(message = "Delivery person name cannot be blank.")
	@Size(max = 30, message = "Delivery person name cannot be greater than 30 characters.")
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
	private Boolean receiptStatus;

}
