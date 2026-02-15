package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.PaymentRequest;
import com.Krishnendu.BillingSoftware.io.PaymentVerificationRequest;
import com.Krishnendu.BillingSoftware.io.RazorpayOrderResponse;
import com.Krishnendu.BillingSoftware.service.impl.OrderServiceImpl;
import com.Krishnendu.BillingSoftware.service.impl.RazorpayServiceImpl;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final RazorpayServiceImpl razorpayService;
    private final OrderServiceImpl orderService;

    @PostMapping("/create-order")
    public ResponseEntity<RazorpayOrderResponse> createRazorpayOrder(@RequestBody PaymentRequest paymentRequest) throws RazorpayException {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(razorpayService.createOrder(paymentRequest.getAmount(), paymentRequest.getCurrency()));


    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.verifyPayment(request));

    }
}
