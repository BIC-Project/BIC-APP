package com.bic.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

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
		return (Long)
				entityManager
				.createQuery("select count(c.id) from Customer c where c.customerName = :customerName") 
				.setParameter("customerName", customerName)
				.getSingleResult() == 1 ? true : false;
	}
	
//	public List<Customer> findAll() {
//	    EntityManagerFactory factory = Persistence
//	            .createEntityManagerFactory("persistenceUnitName");
//	    entityManager = factory.createEntityManager();
//	    entityManager.getTransaction().begin();
//	    List<Customer> customerList = entityManager.createQuery(
//	            "SELECT p FROM customer_tbl p ").getResultList();
//	    entityManager.getTransaction().commit();
//	    entityManager.close();
//	    factory.close();
//	    if (customerList == null) {
//	        System.out.println("No persons found . ");
//	    } else {
////	        for (Customer Customer : customerList) {
//////	        System.out.print("Person name= " + Customer.getName()
//////	                + ", gender" + person.getGender() + ", birthday="
//////	                + person.getBirthday());
////	        }
//	    }
//
//	    return customerList;
//	    }
//	
}