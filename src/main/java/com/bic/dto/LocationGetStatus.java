package com.bic.dto;

import com.bic.entity.Location;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LocationGetStatus extends Status {

    private Location location;
}
