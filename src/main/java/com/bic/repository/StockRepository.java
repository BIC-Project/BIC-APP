package com.bic.repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.bic.dto.CylinderStockQty;
import com.bic.entity.CompositeLocationCustomerCylinder;
import com.bic.entity.Customer;
import com.bic.entity.Cylinder;
import com.bic.entity.Location;
import com.bic.entity.ReceiptType;
import com.bic.entity.Stock;
import com.bic.exception.StockServiceException;

@Repository
public class StockRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public boolean saveAll(CylinderStockQty[] allCylinderStock,
			Location location, Customer customer, ReceiptType receiptType) {

		for (CylinderStockQty cylinderStockQty : allCylinderStock) {
			Cylinder cylinder = new Cylinder();
			cylinder.setCylinderId(cylinderStockQty.getCylinderId());
			CompositeLocationCustomerCylinder compositeCustomerCylinderPK = new CompositeLocationCustomerCylinder(
					location, customer, cylinder);
			entityManager.find(Stock.class, compositeCustomerCylinderPK,
					LockModeType.PESSIMISTIC_WRITE);
		}
		for (CylinderStockQty cylinderStockQty : allCylinderStock) {
			Cylinder cylinder = new Cylinder();
			cylinder.setCylinderId(cylinderStockQty.getCylinderId());
			int cylinderStock = cylinderStockQty.getCylinderStock();

			CompositeLocationCustomerCylinder compositeCustomerCylinderPK = new CompositeLocationCustomerCylinder(
					location, customer, cylinder);
			Stock stock = entityManager.find(Stock.class,
					compositeCustomerCylinderPK);
			if (stock == null && receiptType.equals(ReceiptType.ER)) {
				Stock newStock = new Stock(compositeCustomerCylinderPK,
						cylinderStock);
				entityManager.persist(newStock);
			} else {
				if (receiptType.equals(ReceiptType.ER)) {
					int oldCylinderStock = stock.getCylinderStock();
					stock.setCylinderStock(oldCylinderStock + cylinderStock);
				} else {
					if (stock == null)
						throw new StockServiceException(
								"Error! Delivery cylinder qty is greater than stock qty.");
					int oldCylinderStock = stock.getCylinderStock();
					if (cylinderStock > oldCylinderStock)
						throw new StockServiceException(
								"Error! Delivery cylinder qty is greater than stock qty.");
					stock.setCylinderStock(oldCylinderStock - cylinderStock);
				}
			}
		}
		return true;

	}

	public boolean deleteAll(CylinderStockQty[] allCylinderStock,
			Location location, Customer customer, ReceiptType receiptType) {

		for (CylinderStockQty cylinderStockQty : allCylinderStock) {
			Cylinder cylinder = new Cylinder();
			cylinder.setCylinderId(cylinderStockQty.getCylinderId());
			CompositeLocationCustomerCylinder compositeCustomerCylinderPK = new CompositeLocationCustomerCylinder(
					location, customer, cylinder);
			entityManager.find(Stock.class, compositeCustomerCylinderPK,
					LockModeType.PESSIMISTIC_WRITE);
		}
		for (CylinderStockQty cylinderStockQty : allCylinderStock) {
			Cylinder cylinder = new Cylinder();
			int cylinderStock = cylinderStockQty.getCylinderStock();
			cylinder.setCylinderId(cylinderStockQty.getCylinderId());
			CompositeLocationCustomerCylinder compositeCustomerCylinderPK = new CompositeLocationCustomerCylinder(
					location, customer, cylinder);
			Stock stock = entityManager.find(Stock.class,
					compositeCustomerCylinderPK);

			if (receiptType.equals(ReceiptType.DR)) {
				int oldCylinderStock = stock.getCylinderStock();
				stock.setCylinderStock(oldCylinderStock + cylinderStock);
			} else {
				int oldCylinderStock = stock.getCylinderStock();
				if (cylinderStock > oldCylinderStock)
					throw new StockServiceException(
							"Error deleting receipt! Cannot delete receipt.");
				stock.setCylinderStock(oldCylinderStock - cylinderStock);
			}
		}
		return true;

	}

}
