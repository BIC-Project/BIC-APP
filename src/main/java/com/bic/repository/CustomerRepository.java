package com.bic.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bic.entity.Customer;

@Repository
public class CustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public int save(Customer customer) {
	Customer updatedCustomer = entityManager.merge(customer);
	return updatedCustomer.getCustomerId();
    }

    public Customer fetch(int customerId) {
	return entityManager.find(Customer.class, customerId);
    }

    public boolean isCustomerPresent(String customerName) {
	return (Long) entityManager
		.createQuery("select count(c.id) from Customer c where c.customerName = :customerName")
		.setParameter("customerName", customerName).getSingleResult() == 1 ? true : false;
    }

    public boolean isCustomerPresent(int customerId) {
	return (Long) entityManager.createQuery("select count(c.id) from Customer c where c.customerId = :customerId")
		.setParameter("customerId", customerId).getSingleResult() == 1 ? true : false;
    }

    public List<Customer> findAll() {
	Session session = entityManager.unwrap(Session.class);
	CriteriaBuilder cb = session.getCriteriaBuilder();
	CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
	Root<Customer> rootEntry = cq.from(Customer.class);
	CriteriaQuery<Customer> all = cq.select(rootEntry);

	TypedQuery<Customer> allQuery = session.createQuery(all);
	return allQuery.getResultList();
    }

//   public int modifyCustomer(Customer customer) {
//	   return entityManager.createQuery( "update Customer set customerAddress = customerAddress, customerPinCode = customerPinCode, "
//	   		+ "contactName1 = contactName1, contactName2 = contactName2, contactNumber1 = contactNumber1, contactNumber2 = contactNumber2, "
//	   		+ "isActive = isActive " ).executeUpdate();
//   }

    public void modifyCustomer(Customer customer) {
	CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

	// create update
	CriteriaUpdate<Customer> update = cb.createCriteriaUpdate(Customer.class);

	// set the root class
	Root<Customer> e = update.from(Customer.class);

	// set update and where clause
	update.set("customerAddress", customer.getCustomerAddress());
	update.set("customerPinCode", customer.getCustomerPinCode());
	update.set("contactName1", customer.getContactName1());
	update.set("contactName2", customer.getContactName2());
	update.set("contactNumber1", customer.getContactNumber1());
	update.set("contactNumber2", customer.getContactNumber2());
	// update.where(cb.greaterThanOrEqualTo(e.get("customerId"),
	// customer.getCustomerId()));
	update.where(cb.equal(e.get("customerId"), customer.getCustomerId()));
	System.out.println(e.get("customerId") + " " + customer.getCustomerId());
	// perform update
	this.entityManager.createQuery(update).executeUpdate();
    }

}