package com.bic.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class StockController {

	// private Stock
	//
	// @GetMapping("/cylinder")
	// @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	// public ResponseEntity<CylinderGetAllStatus> getAllCylinder() {
	// CylinderGetAllStatus status = new CylinderGetAllStatus();
	// try {
	// List<Cylinder> allCylinder = cylinderService.getAll();
	// status.setAllCylinder(allCylinder);
	// status.setStatus(StatusType.SUCCESS);
	// status.setMessage("Cylinders Found");
	// return new ResponseEntity<>(status, HttpStatus.OK);
	// } catch (CylinderServiceException e) {
	// status.setStatus(StatusType.FAILURE);
	// status.setMessage(e.getMessage());
	// return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
	// }
	// }
}
