package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.OrderRequest;
import com.Krishnendu.BillingSoftware.io.OrderResponse;
import com.Krishnendu.BillingSoftware.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderRequest));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") String orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<OrderResponse>> getLatestOrder() {
         return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getLatestOrder());
    }
}
