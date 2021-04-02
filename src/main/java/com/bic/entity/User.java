package com.bic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_TBL")
public class User {

	@Id
	@Column(columnDefinition = "varchar(20)")
	private String userName;

	@Column(columnDefinition = "varchar(20)", nullable = false)
	private String password;
	@Column(nullable = false, columnDefinition = "varchar(20)")
	private String roles;
}
