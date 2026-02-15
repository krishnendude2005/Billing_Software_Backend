package com.Krishnendu.BillingSoftware.io;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private String customerName;
    private String phoneNumber;
    private List<OrderItemRequest> cartItems;
    private Double subTotal;
    private Double tax;
    private Double grandTotal;
    private String paymentMethod;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItemRequest {
         private String itemId;
         private String name;
         private Double price;
         private Integer quantity;
    }

}
