package com.bic.dto;

import com.bic.entity.Cylinder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CylinderGetStatus extends Status {

    private Cylinder cylinder;

}
