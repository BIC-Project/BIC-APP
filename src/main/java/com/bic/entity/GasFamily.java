package com.bic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GAS_FAMILY_TBL")
public class GasFamily {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gasFamilyId;
    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String gasFamilyName;
}
