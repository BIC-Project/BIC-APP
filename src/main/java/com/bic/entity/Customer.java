package com.bic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER_TBL")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int customerId;

	@Column(nullable = false, columnDefinition = "varchar(30)", unique = true)
	@NotBlank(message = "Customer name cannot be empty.")
	@Size(min = 3, max = 30, message = "Customer name should be between 3 to 30 characters.")
	private String customerName;

	@Column(nullable = false, columnDefinition = "varchar(200)")
	@NotBlank(message = "Customer address cannot be empty.")
	@Size(min = 3, max = 200, message = "Customer address should be between 3 to 200 characters.")
	private String customerAddress;

	@Column(nullable = false, columnDefinition = "varchar(7)")
	@Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pincode should be 6 digits. Ex. 416416")
	@NotBlank(message = "Pincode cannot be empty.")
	private String customerPinCode;

	@Column(nullable = false, columnDefinition = "varchar(30)")
	@NotBlank(message = "Contact person name cannot be empty.")
	@Size(min = 3, max = 30, message = "Contact person name should be between 3 to 30 characters.")
	private String contactName1;

	@Column(nullable = true, columnDefinition = "varchar(30)")
	@Size(min = 3, max = 30, message = "Contact person name should be between 3 to 30 characters.")
	private String contactName2;

	@Column(nullable = false, columnDefinition = "varchar(10)")
	@NotBlank(message = "Contact person number cannot be empty.")
	@Pattern(regexp = "^[0-9]{3,10}$", message = "Contact number should only contain digits. Ex. 91XXXXXXXX")
	@Size(min = 3, max = 10, message = "Contact number should be between 3 and 10 digits.")
	private String contactNumber1;

	@Column(nullable = true, columnDefinition = "varchar(10)")
	@Pattern(regexp = "^[0-9]{3,10}$", message = "Contact number should only contain digits. Ex. 91XXXXXXXX")
	@Size(min = 3, max = 10, message = "Contact number should be between 3 and 10 digits.")
	private String contactNumber2;

	@Column(nullable = false)
	private Boolean isActive;

}
