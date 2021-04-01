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
	@Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pin Code should be 6 digits. Ex. 416416")
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
	private boolean isActive;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerPinCode() {
		return customerPinCode;
	}

	public void setCustomerPinCode(String customerPinCode) {
		this.customerPinCode = customerPinCode;
	}

	public String getContactName1() {
		return contactName1;
	}

	public void setContactName1(String contactName1) {
		this.contactName1 = contactName1;
	}

	public String getContactName2() {
		return contactName2;
	}

	public void setContactName2(String contactName2) {
		this.contactName2 = contactName2;
	}

	public String getContactNumber1() {
		return contactNumber1;
	}

	public void setContactNumber1(String contactNumber1) {
		this.contactNumber1 = contactNumber1;
	}

	public String getContactNumber2() {
		return contactNumber2;
	}

	public void setContactNumber2(String contactNumber2) {
		this.contactNumber2 = contactNumber2;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
