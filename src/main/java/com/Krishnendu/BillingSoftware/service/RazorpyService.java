package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.RazorpayOrderResponse;
import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;

public interface RazorpyService {

    RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException;
}
