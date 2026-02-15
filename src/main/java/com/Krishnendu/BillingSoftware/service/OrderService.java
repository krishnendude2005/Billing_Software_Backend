package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.OrderRequest;
import com.Krishnendu.BillingSoftware.io.OrderResponse;
import com.Krishnendu.BillingSoftware.io.PaymentVerificationRequest;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    void deleteOrder(String orderId);
    List<OrderResponse> getLatestOrder();

     OrderResponse verifyPayment(PaymentVerificationRequest request);
}
