package com.bic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bic.entity.Cylinder;
import com.bic.exception.CylinderServiceException;
import com.bic.repository.CylinderRepository;

@Service
@Transactional
public class CylinderService {

	@Autowired
	private CylinderRepository cylinderRepository;

	public Cylinder get(int cylinderId) {
		Cylinder cylinder = cylinderRepository.findByCylinderId(cylinderId);
		if (cylinder != null) {
			return cylinder;
		} else {
			throw new CylinderServiceException("Cylinder id is invalid.");
		}
	}

	public List<Cylinder> getAll() {
		List<Cylinder> allCylinders = cylinderRepository.findAll();
		if (!allCylinders.isEmpty()) {
			return allCylinders;
		} else {
			throw new CylinderServiceException("No cylinders found.");
		}
	}
}
