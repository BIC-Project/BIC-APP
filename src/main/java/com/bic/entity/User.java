package com.bic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_TBL")
public class User {

    @Id
    @Column(columnDefinition = "varchar(20)")
    private String userName;
    @Column(columnDefinition = "varchar(20)")
    private String password;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String roles;

    public User() {
	super();
	// TODO Auto-generated constructor stub
    }

    public User(String userName, String password) {
	super();
	this.userName = userName;
	this.password = password;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getRoles() {
	return roles;
    }

    public void setRoles(String roles) {
	this.roles = roles;
    }

    public User(String userName, String password, String roles) {
	super();
	this.userName = userName;
	this.password = password;
	this.roles = roles;
    }

    @Override
    public String toString() {
	return "User [userName=" + userName + ", password=" + password + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((password == null) ? 0 : password.hashCode());
	result = prime * result + ((userName == null) ? 0 : userName.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	User other = (User) obj;
	if (password == null) {
	    if (other.password != null)
		return false;
	} else if (!password.equals(other.password))
	    return false;
	if (userName == null) {
	    if (other.userName != null)
		return false;
	} else if (!userName.equals(other.userName))
	    return false;
	return true;
    }

}
