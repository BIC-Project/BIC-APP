package com.bic.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ValidationErrorStatus extends Status {

	Map<String, String> errors = new HashMap<>();

}
