package com.bic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bic.dto.LocationGetAllStatus;
import com.bic.dto.LocationGetStatus;
import com.bic.dto.Status.StatusType;
import com.bic.entity.Location;
import com.bic.exception.LocationServiceException;
import com.bic.service.LocationService;

@RestController
@CrossOrigin
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping("/location")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<LocationGetAllStatus> getAllLocation() {
		LocationGetAllStatus status = new LocationGetAllStatus();
		try {
			List<Location> allLocation = locationService.getAll();
			status.setAllLocation(allLocation);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Location found.");
			return new ResponseEntity<>(status, HttpStatus.OK);
		} catch (LocationServiceException e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/location/{locationId}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<LocationGetStatus> getLocation(
			@PathVariable int locationId) {
		LocationGetStatus status = new LocationGetStatus();
		try {
			Location location = locationService.get(locationId);
			status.setLocation(location);
			status.setStatus(StatusType.SUCCESS);
			status.setMessage("Location found.");
			return new ResponseEntity<>(status, HttpStatus.OK);
		} catch (LocationServiceException e) {
			status.setStatus(StatusType.FAILURE);
			status.setMessage(e.getMessage());
			return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
		}
	}
}
