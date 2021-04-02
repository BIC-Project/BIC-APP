package com.bic.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = -502703236534477933L;
	private String userName;
	private String password;
}
