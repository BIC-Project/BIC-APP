package com.bic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GAS_TBL")
public class Gas {

    @Id
    @Column(columnDefinition = "varchar(10)")
    private String gasId;

    @Column(columnDefinition = "varchar(30)", unique = true, nullable = false)
    private String gasName;

    @ManyToOne
    @JoinColumn(name = "gasFamilyId", referencedColumnName = "gasFamilyId", nullable = false)
    private GasFamily gasFamily;

}
