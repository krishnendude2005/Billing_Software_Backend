package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.entity.OrderEntity;
import com.Krishnendu.BillingSoftware.entity.OrderItemEntity;
import com.Krishnendu.BillingSoftware.io.*;
import com.Krishnendu.BillingSoftware.repository.OrderEntityRepo;
import com.Krishnendu.BillingSoftware.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderEntityRepo orderRepo;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        try {
            // Convert to orderEntity
            OrderEntity newOrder = convertToOrderEntity(orderRequest);

            // Set the payment details
            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setStatus(newOrder.getPaymentMethod() == PaymentMethod.CASH ?
                    PaymentDetails.PaymentStatus.COMPLETED : PaymentDetails.PaymentStatus.PENDING);

            newOrder.setPaymentDetails(paymentDetails);

            // Get the order items
            List<OrderItemEntity> orderItems = orderRequest.getCartItems().stream()
                    .map(item -> convertToOrderItemEntity(item))
                    .toList();

            newOrder.setOrderItems(orderItems);

            newOrder = orderRepo.save(newOrder);

            return convertToOrderResponse(newOrder);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create order");
        }

    }

    private OrderItemEntity convertToOrderItemEntity(OrderRequest.OrderItemRequest orderItemRequest) {
        return OrderItemEntity.builder()
                .itemId(orderItemRequest.getItemId())
                .itemName(orderItemRequest.getName())
                .itemPrice(orderItemRequest.getPrice())
                .itemQuantity(orderItemRequest.getQuantity())
                .build();

    }

    private OrderResponse convertToOrderResponse(OrderEntity newOrder) {
        return OrderResponse.builder()
                .orderId(newOrder.getOrderId())
                .customerName(newOrder.getCustomerName())
                .phoneNumber(newOrder.getPhoneNumber())
                .subTotal(newOrder.getSubTotal())
                .tax(newOrder.getTax())
                .grandTotal(newOrder.getGrandTotal())
                .paymentMethod(newOrder.getPaymentMethod())
                .items(newOrder.getOrderItems().stream().map(item -> convertToItemResponse(item)).collect(Collectors.toList()))
                .paymentDetails(newOrder.getPaymentDetails())
                .createdAt(newOrder.getCreatedAt())
                .build();

    }

    private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity) {
        return OrderResponse.OrderItemResponse.builder()
                .itemId(orderItemEntity.getItemId())
                .name(orderItemEntity.getItemName())
                .price(orderItemEntity.getItemPrice())
                .quantity(orderItemEntity.getItemQuantity())
                .build();
    }

    private OrderEntity convertToOrderEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .customerName(orderRequest.getCustomerName())
                .phoneNumber(orderRequest.getPhoneNumber())
                .subTotal(orderRequest.getSubTotal())
                .tax(orderRequest.getTax())
                .grandTotal(orderRequest.getGrandTotal())
                .paymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()))
                .build();
    }

    @Override
    public void deleteOrder(String orderId) {
        try {
            OrderEntity existingOrder = orderRepo.findByOrderId(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
            orderRepo.delete(existingOrder);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete order with id " + orderId);
        }
    }

    @Override
    public List<OrderResponse> getLatestOrder() {
        return orderRepo.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse verifyPayment(PaymentVerificationRequest request) {
        OrderEntity existingOrder = orderRepo.findByOrderId(request.getOrderId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!verifyRazorpaySignature(request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Razorpay Signature");

        }

        PaymentDetails paymentDetails = existingOrder.getPaymentDetails();
        paymentDetails.setRazorpayOrderId(request.getOrderId());
        paymentDetails.setRazorpayPaymentId(request.getRazorpayPaymentId());
        paymentDetails.setRazorpaySignature(request.getRazorpaySignature());
        paymentDetails.setStatus(PaymentDetails.PaymentStatus.COMPLETED);


        existingOrder = orderRepo.save(existingOrder);
        return convertToOrderResponse(existingOrder);

    }

    @Override
    public Double sumSalesByDate(LocalDate date) {
        try {
            return orderRepo.sumSalesByDate(date);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch sales for date " + date);
        }
    }

    @Override
    public Long countByOrderDate(LocalDate date) {
        try {
            return orderRepo.countByOrderDate(date);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch count for date " + date);
        }
    }


    @Override
    public List<OrderResponse> findRecentOrders() {
        try {
            Pageable pageable = PageRequest.of(0,5);
            return orderRepo.findRecentOrders(pageable)
                    .stream()
                    .map(orderEntity -> convertToResponse(orderEntity))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch recent orders");
        }
    }

    private OrderResponse convertToResponse(OrderEntity orderEntity) {
        return OrderResponse.builder()
                .orderId(orderEntity.getOrderId())
                .customerName(orderEntity.getCustomerName())
                .phoneNumber(orderEntity.getPhoneNumber())
                .subTotal(orderEntity.getSubTotal())
                .tax(orderEntity.getTax())
                .grandTotal(orderEntity.getGrandTotal())
                .paymentMethod(orderEntity.getPaymentMethod())
                .paymentDetails(orderEntity.getPaymentDetails())
                .createdAt(orderEntity.getCreatedAt())
                .build();
    }

    private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        return true;
    }
}
