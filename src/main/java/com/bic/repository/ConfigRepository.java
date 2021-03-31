package com.bic.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.bic.entity.Config;

public interface ConfigRepository
        extends
            JpaRepository<Config, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Config findByConfigKey(String configKey);
}
