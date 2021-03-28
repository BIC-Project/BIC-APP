package com.bic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.bic.service.ReceiptService;

@RestController
@CrossOrigin
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;
}
