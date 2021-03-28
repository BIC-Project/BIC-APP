package com.bic.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.Gas;
import com.bic.entity.GasFamily;

public interface GasRepository extends JpaRepository<Gas, String> {
    public Gas findByGasId(String gasId);

    public Gas findByGasName(String gasName);

    public ArrayList<Gas> findByGasFamily(GasFamily gasFamily);
}
