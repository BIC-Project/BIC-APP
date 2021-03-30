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

    public boolean saveAll(
            CylinderStockQty[] allCylinderStock,
            Customer customer, ReceiptType receiptType) {

        try {
            for (CylinderStockQty cylinderStockQty : allCylinderStock) {
                Cylinder cylinder = new Cylinder();
                cylinder.setCylinderId(
                        cylinderStockQty.getCylinderId());
                CompositeCustomerCylinder compositeCustomerCylinderPK = new CompositeCustomerCylinder(
                        customer,
                        cylinder);
                entityManager.find(Stock.class,
                        compositeCustomerCylinderPK,
                        LockModeType.PESSIMISTIC_WRITE);
            }
            for (CylinderStockQty cylinderStockQty : allCylinderStock) {
                Cylinder cylinder = new Cylinder();
                int cylinderStock = cylinderStockQty
                        .getCylinderStock();
                cylinder.setCylinderId(
                        cylinderStockQty.getCylinderId());
                CompositeCustomerCylinder compositeCustomerCylinderPK = new CompositeCustomerCylinder(
                        customer,
                        cylinder);
                Stock stock = entityManager.find(
                        Stock.class,
                        compositeCustomerCylinderPK);
                if (stock == null) {
                    Stock newStock = new Stock(
                            compositeCustomerCylinderPK,
                            cylinderStock);
                    entityManager.persist(newStock);
                } else {
                    if (receiptType
                            .equals(ReceiptType.ER)) {
                        int oldCylinderStock = stock
                                .getCylinderStock();
                        stock.setCylinderStock(
                                oldCylinderStock
                                        + cylinderStock);
                    } else {
                        int oldCylinderStock = stock
                                .getCylinderStock();
                        if (cylinderStock > oldCylinderStock)
                            return false;
                        stock.setCylinderStock(
                                oldCylinderStock
                                        - cylinderStock);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
