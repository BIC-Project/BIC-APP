package com.bic.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.Cylinder;
import com.bic.entity.CylinderUnit;
import com.bic.entity.Gas;

public interface CylinderRepository extends JpaRepository<Cylinder, Integer> {

    public Cylinder findByCylinderId(int cylinderId);

    public ArrayList<Cylinder> findByGas(Gas gas);

    public ArrayList<Cylinder> findByCylinderCapacity(double cylinderCapacity);

    public ArrayList<Cylinder> findByCylinderUnit(CylinderUnit cylinderUnit);
}
