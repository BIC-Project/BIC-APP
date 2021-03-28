package com.bic.repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.bic.dto.CylinderStockQty;
import com.bic.entity.CompositeCustomerCylinder;
import com.bic.entity.Customer;
import com.bic.entity.Cylinder;
import com.bic.entity.ReceiptType;
import com.bic.entity.Stock;

@Repository

public class StockRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean saveAll(CylinderStockQty[] allCylinderStock, Customer customer, ReceiptType receiptType) {

//	Session session = entityManager.unwrap(Session.class);
//	Transaction tr = session.beginTransaction();

	try {

	    for (CylinderStockQty cylinderStockQty : allCylinderStock) {
		Cylinder cylinder = new Cylinder();
		cylinder.setCylinderId(cylinderStockQty.getCylinderId());
		CompositeCustomerCylinder compositeCustomerCylinderPK = new CompositeCustomerCylinder(customer,
			cylinder);
		entityManager.find(Stock.class, compositeCustomerCylinderPK, LockModeType.PESSIMISTIC_READ);
//		Query query = session
//			.createSQLQuery("select * from stock_tbl where cylinder_id = ? and customer_id = ? for update");
//		query.setParameter(0, cylinderStockQty.getCylinderId());
//		query.setParameter(1, customer.getCustomerId());
//		query.executeUpdate();
	    }

	    for (CylinderStockQty cylinderStockQty : allCylinderStock) {
		Cylinder cylinder = new Cylinder();
		int cylinderStock = cylinderStockQty.getCylinderStock();
		cylinder.setCylinderId(cylinderStockQty.getCylinderId());
		CompositeCustomerCylinder compositeCustomerCylinderPK = new CompositeCustomerCylinder(customer,
			cylinder);
		Stock stock = entityManager.find(Stock.class, compositeCustomerCylinderPK);
		if (stock == null) {
		    Stock newStock = new Stock(compositeCustomerCylinderPK, cylinderStock);
		    entityManager.persist(newStock);
		} else {
		    if (receiptType.equals(ReceiptType.ER)) {
			int oldCylinderStock = stock.getCylinderStock();
			// adding of stock
			stock.setCylinderStock(oldCylinderStock + cylinderStock);

		    } else {
			int oldCylinderStock = stock.getCylinderStock();
			if (cylinderStock > oldCylinderStock) {
			    // entityManager.getTransaction().rollback();
			    return false;
			}
			stock.setCylinderStock(oldCylinderStock - cylinderStock);
		    }
		}
	    }
	    // entityManager.getTransaction().commit();
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    // entityManager.getTransaction().rollback();
	    return false;
	}
    }

    public boolean save(Customer customer, Cylinder cylinder, int cylinderStock, ReceiptType receiptType) {
	try {
	    entityManager.getTransaction().begin();
	    CompositeCustomerCylinder compositeCustomerCylinderPK = new CompositeCustomerCylinder(customer, cylinder);

	    Stock stock = entityManager.find(Stock.class, compositeCustomerCylinderPK, LockModeType.PESSIMISTIC_WRITE);
	    if (stock == null) {
		Stock newStock = new Stock(compositeCustomerCylinderPK, cylinderStock);
		entityManager.persist(newStock);
		entityManager.getTransaction().commit();
		return true;
	    } else {
		if (receiptType.equals(ReceiptType.ER)) {
		    int oldCylinderStock = stock.getCylinderStock();
		    // adding of stock
		    stock.setCylinderStock(oldCylinderStock + cylinderStock);
		    entityManager.getTransaction().commit();
		    return true;
		} else {
		    int oldCylinderStock = stock.getCylinderStock();
		    if (cylinderStock > oldCylinderStock) {
			entityManager.getTransaction().rollback();
			return false;
		    }
		    stock.setCylinderStock(oldCylinderStock - cylinderStock);
		    entityManager.getTransaction().commit();
		    return true;
		}
	    }
	} catch (Exception e) {
	    entityManager.getTransaction().rollback();
	    return false;
	} finally {
	    entityManager.flush();
	}
    }

}

//
//boolean isStockSaved;
//for (CylinderStockQty cylinderStockQty : allCylinderStock) {
//    Customer customer = receipt.getCustomer();
//
//    Cylinder cylinder = new Cylinder();
//    cylinder.setCylinderId(cylinderStockQty.getCylinderId());
//
//    int cylinderStock = cylinderStockQty.getCylinderStock();
//
//    ReceiptType receiptType = receipt.getReceiptType();
//    isStockSaved = stockRepository.save(customer, cylinder, cylinderStock, receiptType);
//
//}
//
//Stock stock = entityManager.find(Stock.class, compositeCustomerCylinderPK, LockModeType.PESSIMISTIC_WRITE);

// Customer customer = receipt.getCustomer();
// ReceiptType receiptType = receipt.getReceiptType();
// String allCylinderStrJSON = receipt.getAllCylinders();
// Gson gson = new Gson();
// CylinderStockQty[] allCylinderStock = gson.fromJson(allCylinderStrJSON,
// CylinderStockQty[].class);

//if (allCylinderStrJSON == null || allCylinderStock.length <= 0) {
//	entityManager.getTransaction().rollback();
//	return false;
//}