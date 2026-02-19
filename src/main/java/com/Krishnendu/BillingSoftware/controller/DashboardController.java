package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.DashboardResponse;
import com.Krishnendu.BillingSoftware.io.OrderResponse;
import com.Krishnendu.BillingSoftware.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardData() {
        LocalDate today = LocalDate.now();
        Double salesToday = orderService.sumSalesByDate(today);
        Long todayOrderCount = orderService.countByOrderDate(today);
        List<OrderResponse> recentOrders = orderService.findRecentOrders();

        return ResponseEntity
                .status(200)
                .body(new DashboardResponse(salesToday,todayOrderCount,recentOrders));
    }
}
