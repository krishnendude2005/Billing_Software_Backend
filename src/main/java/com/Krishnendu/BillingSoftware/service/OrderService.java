package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.entity.OrderEntity;
import com.Krishnendu.BillingSoftware.io.OrderRequest;
import com.Krishnendu.BillingSoftware.io.OrderResponse;
import com.Krishnendu.BillingSoftware.io.PaymentVerificationRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    void deleteOrder(String orderId);
    List<OrderResponse> getLatestOrder();

     OrderResponse verifyPayment(PaymentVerificationRequest request);

     Double sumSalesByDate(LocalDate date);

     Long countByOrderDate(LocalDate date);

     List<OrderResponse> findRecentOrders();

}
