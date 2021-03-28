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

import com.bic.dto.CylinderGetAllStatus;
import com.bic.dto.CylinderGetStatus;
import com.bic.dto.Status.StatusType;
import com.bic.entity.Cylinder;
import com.bic.exception.CylinderServiceException;
import com.bic.service.CylinderService;

@RestController
@CrossOrigin
public class CylinderController {

    @Autowired
    private CylinderService cylinderService;

    @GetMapping("/cylinder")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CylinderGetAllStatus> getAllCylinder() {
	CylinderGetAllStatus status = new CylinderGetAllStatus();
	try {
	    List<Cylinder> allCylinder = cylinderService.getAll();
	    status.setAllCylinder(allCylinder);
	    status.setStatus(StatusType.SUCCESS);
	    status.setMessage("Cylinders Found");
	    return new ResponseEntity<>(status, HttpStatus.OK);
	} catch (CylinderServiceException e) {
	    status.setStatus(StatusType.FAILURE);
	    status.setMessage(e.getMessage());
	    return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
	}
    }

    @GetMapping("/cylinder/{cylinderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CylinderGetStatus> getCylinder(@PathVariable int cylinderId) {
	CylinderGetStatus status = new CylinderGetStatus();
	try {
	    Cylinder cylinder = cylinderService.get(cylinderId);
	    status.setCylinder(cylinder);
	    status.setStatus(StatusType.SUCCESS);
	    status.setMessage("Cylinder Found");
	    return new ResponseEntity<>(status, HttpStatus.OK);
	} catch (CylinderServiceException e) {
	    status.setStatus(StatusType.FAILURE);
	    status.setMessage(e.getMessage());
	    return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
	}
    }

}
