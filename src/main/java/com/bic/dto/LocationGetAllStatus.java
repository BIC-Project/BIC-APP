package com.bic.dto;

import java.util.List;

import com.bic.entity.Location;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LocationGetAllStatus extends Status {

    private List<Location> allLocation;
}
