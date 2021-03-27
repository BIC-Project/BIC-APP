package com.bic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    public Location findByLocationId(int locationId);

    public Location findByLocationName(String locationName);

}
