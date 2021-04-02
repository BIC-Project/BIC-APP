package com.bic.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bic.dto.CylinderStockQty;
import com.bic.entity.Config;
import com.bic.entity.Customer;
import com.bic.entity.Receipt;
import com.bic.entity.ReceiptType;
import com.bic.exception.ReceiptServiceException;
import com.bic.repository.ConfigRepository;
import com.bic.repository.CustomerRepository;
import com.bic.repository.CylinderRepository;
import com.bic.repository.LocationRepository;
import com.bic.repository.ReceiptRepository;
import com.bic.repository.StockRepository;
import com.google.gson.Gson;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiptService {

	@Value("${app.max.receipt.no}")
	private Integer maxReceiptNo;
	@Value("${app.config.receiptNoCountER}")
	private String receiptNoCountER;

	@Value("${app.config.receiptNoCountDR}")
	private String receiptNoCountDR;

	@Value("${app.config.maxDeleteHours}")
	private int maxDeleteHours;

	@Autowired
	private ReceiptRepository receiptRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private CylinderRepository cylinderRepository;

	@Autowired
	private ConfigRepository configRepository;

	@Autowired
	private LocationRepository locationRepository;

	public Receipt saveReceipt(Receipt receipt) throws InterruptedException {
		try {
			if (receiptRepository.existsById(receipt.getReceiptId()))
				throw new ReceiptServiceException("Receipt id already exists!");
			System.out.println(receipt.getReceiptType());
			if (receipt.getReceiptType() != ReceiptType.ER
					&& receipt.getReceiptType() != ReceiptType.DR)
				throw new ReceiptServiceException("Receipt type is invalid!");
			if (!(customerRepository
					.existsById(receipt.getCustomer().getCustomerId())))
				throw new ReceiptServiceException(
						"Invalid customer! Please select a valid customer.");
			Optional<Customer> customerOpt = customerRepository
					.findById(receipt.getCustomer().getCustomerId());
			Customer customer = customerOpt.get();
			if (!customer.getIsActive())
				throw new ReceiptServiceException(
						"Selected customer is inactive! Please activate the customer!");

			if (locationRepository.findByLocationId(
					receipt.getLocation().getLocationId()) == null)
				throw new ReceiptServiceException(
						"Invalid location! Please select a valid location.");
			if (receipt.getDateTime() == null)
				throw new ReceiptServiceException(
						"Invalid Date! Please select a valid date.");
			Date parseDate = new Date(
					receipt.getDateTime().getTime() - 330 * 60 * 1000);
			receipt.setDateTime(parseDate);
			ReceiptType receiptType = receipt.getReceiptType();
			String allCylinderStrJSON = receipt.getAllCylinders();
			if (allCylinderStrJSON == null
					|| allCylinderStrJSON.trim().isEmpty())
				throw new ReceiptServiceException("No cylinders found.");
			Gson gson = new Gson();
			CylinderStockQty[] allCylinderStock = gson
					.fromJson(allCylinderStrJSON, CylinderStockQty[].class);
			if (allCylinderStock.length <= 0)
				throw new ReceiptServiceException("No cylinders found.");
			for (CylinderStockQty cylinderStockQty : allCylinderStock) {
				int cylinderId = cylinderStockQty.getCylinderId();
				if (!cylinderRepository.existsById(cylinderId))
					throw new ReceiptServiceException("Illegal cylinder id.");
				if (cylinderStockQty.getCylinderStock() <= 0)
					throw new ReceiptServiceException("Illegal cylinder qty.");
			}
			boolean isStockSaved = stockRepository.saveAll(allCylinderStock,
					customer, receiptType);
			if (isStockSaved) {
				Config config = null;
				if (receipt.getReceiptType().equals(ReceiptType.ER))
					config = configRepository.findByConfigKey(receiptNoCountER);
				else if (receipt.getReceiptType().equals(ReceiptType.DR))

					config = configRepository.findByConfigKey(receiptNoCountDR);
				if (config == null)
					throw new ReceiptServiceException(
							"Error! Please try again");
				else {
					int currentReceiptNo = Integer
							.parseInt(config.getConfigValue());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(receipt.getDateTime());

					receipt.setReceiptNo(
							"BIC-" + receipt.getReceiptType() + "-"
									+ String.format("%02d",
											(calendar.get(Calendar.MONTH)) + 1)
									+ "-"
									+ String.format(
											"%02d", calendar.get(Calendar.YEAR))
									+ "-"
									+ String.format(
											"%0" + String.valueOf(maxReceiptNo)
													.length() + "d",
											currentReceiptNo));
					System.out.println(receipt.getReceiptNo());
					if (currentReceiptNo >= maxReceiptNo) {
						config.setConfigValue("1");
					} else {
						currentReceiptNo++;
						config.setConfigValue(String.valueOf(currentReceiptNo));
					}
				}
				receipt.setReceiptStatus(true);
				receiptRepository.save(receipt);
			} else
				throw new ReceiptServiceException(
						"Error saving receipt! Invalid receipt.");
			return receipt;
		} catch (Exception e) {
			throw new ReceiptServiceException(e.getMessage());
		}

	}

	public Page<Receipt> getReceiptList(Boolean receiptStatus,
			String receiptType, String customerId, String fromDateTime,
			String toDateTime, String pageNoStr, String sizeStr) {
		if (receiptType == null)
			throw new ReceiptServiceException(
					"Receipt type not selected! Please select a valid receipt type.");
		if (!receiptType.equals(ReceiptType.ER.toString())
				&& !receiptType.equals(ReceiptType.DR.toString()))
			throw new ReceiptServiceException("Receipt type is invalid!");
		if (receiptStatus == null)
			receiptStatus = true;
		if (fromDateTime == null || fromDateTime.trim().isEmpty()
				|| toDateTime == null || toDateTime.trim().isEmpty())
			throw new ReceiptServiceException(
					"Date is empty! Please select appropriate date.");
		Date fromDate, toDate;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			fromDate = formatter.parse(fromDateTime);
			toDate = formatter.parse(toDateTime);
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.HOUR, 23);
			c.add(Calendar.MINUTE, 59);
			c.add(Calendar.SECOND, 59);
			toDate = c.getTime();
		} catch (Exception e) {
			throw new ReceiptServiceException(
					"Invalid date! Please select appropriate date.");
		}

		if (fromDate.compareTo(toDate) > 0)
			throw new ReceiptServiceException(
					"Invalid date! End-date greater than start-date.");

		int pageNo = 0;
		int size = 10;
		try {
			if (pageNoStr != null && !pageNoStr.trim().isEmpty()
					&& !pageNoStr.trim().equals("0"))
				pageNo = Math.abs(Integer.parseInt(pageNoStr));
			if (sizeStr != null && !sizeStr.trim().isEmpty())
				size = Math.abs(Integer.parseInt(sizeStr));
		} catch (Exception e) {
			throw new ReceiptServiceException("Invalid page no.");
		}
		Pageable page = PageRequest.of(pageNo, size,
				Sort.by("dateTime").descending());
		if (customerId == null || customerId.trim().isEmpty()) {

			Page<Receipt> lr = receiptRepository
					.findByReceiptStatusAndReceiptTypeAndDateTimeBetween(
							receiptStatus, ReceiptType.valueOf(receiptType),
							fromDate, toDate, page);
			if (lr == null || !lr.hasContent())
				throw new ReceiptServiceException("No receipt found.");
			return lr;
		}
		try {
			Integer custId = Integer.parseInt(customerId);
			if (!customerRepository.existsById(custId))
				throw new ReceiptServiceException();
			Customer customer = new Customer();
			customer.setCustomerId(custId);

			Page<Receipt> lr = receiptRepository
					.findByReceiptStatusAndReceiptTypeAndCustomerAndDateTimeBetween(
							receiptStatus, ReceiptType.valueOf(receiptType),
							customer, fromDate, toDate, page);
			if (lr == null)
				throw new ReceiptServiceException("No receipt found.");
			return lr;
		} catch (Exception e) {
			throw new ReceiptServiceException(e.getMessage());
		}
	}

	public Receipt deleteReceipt(int receiptId, String currDateTime)
			throws ParseException {

		Optional<Receipt> optReceipt = receiptRepository.findById(receiptId);
		if (!optReceipt.isPresent())
			throw new ReceiptServiceException("Receipt not found.");

		else {
			Receipt receipt = optReceipt.get();
			if (receipt.getReceiptStatus() == false)
				throw new ReceiptServiceException(
						"Receipt is already deleted.");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date currdate = formatter.parse(currDateTime);

			long diff = currdate.getTime() - receipt.getDateTime().getTime();
			diff = diff / 3600000;
			if (diff < 0)
				throw new ReceiptServiceException("Invalid current date!");
			if (diff > maxDeleteHours)
				throw new ReceiptServiceException(
						"Cannot delete a receipt older than "
								+ (maxDeleteHours / 24) + " days");
			String allCylinderStrJSON = receipt.getAllCylinders();
			if (allCylinderStrJSON == null
					|| allCylinderStrJSON.trim().isEmpty())
				throw new ReceiptServiceException("No Cylinders Found");
			Gson gson = new Gson();
			CylinderStockQty[] allCylinderStock = gson
					.fromJson(allCylinderStrJSON, CylinderStockQty[].class);
			if (allCylinderStock.length <= 0)
				throw new ReceiptServiceException("No cylinders found.");
			for (CylinderStockQty cylinderStockQty : allCylinderStock) {
				int cylinderId = cylinderStockQty.getCylinderId();
				if (!cylinderRepository.existsById(cylinderId))
					throw new ReceiptServiceException("Illegal cylinder id.");
				if (cylinderStockQty.getCylinderStock() <= 0)
					throw new ReceiptServiceException("Illegal cylinder qty.");
			}
			boolean isdeleted = stockRepository.deleteAll(allCylinderStock,
					receipt.getCustomer(), receipt.getReceiptType());
			if (!isdeleted)
				throw new ReceiptServiceException(
						"Cannot delete receipt. Stock quantity deviation.");
			else {
				receipt.setReceiptStatus(false);
				receiptRepository.save(receipt);
			}
			return receipt;
		}
	}
}
