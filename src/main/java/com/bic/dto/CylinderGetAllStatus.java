package com.bic.dto;

import java.util.List;

import com.bic.entity.Cylinder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CylinderGetAllStatus extends Status {

    private List<Cylinder> allCylinder;

}
