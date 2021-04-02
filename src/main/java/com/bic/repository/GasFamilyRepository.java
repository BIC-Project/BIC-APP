package com.bic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.GasFamily;

public interface GasFamilyRepository extends JpaRepository<GasFamily, Integer> {
	public GasFamily findByGasFamilyId(int gasFamilyId);

	public GasFamily findByGasFamilyName(String gasFamilyName);

}
