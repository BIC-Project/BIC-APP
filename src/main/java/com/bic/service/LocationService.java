package com.bic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bic.entity.Location;
import com.bic.exception.LocationServiceException;
import com.bic.repository.LocationRepository;

@Service
@Transactional
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location get(int locationId) {
	Location location = locationRepository.findByLocationId(locationId);
	if (location != null)
	    return location;
	else
	    throw new LocationServiceException("Location ID is invalid");
    }

    public List<Location> getAll() {
	List<Location> allLocations = locationRepository.findAll();
	if (!allLocations.isEmpty()) {
	    return allLocations;
	} else {
	    throw new LocationServiceException("There are No Locations");
	}
    }
}
