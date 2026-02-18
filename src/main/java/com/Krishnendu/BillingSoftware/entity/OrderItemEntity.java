package com.Krishnendu.BillingSoftware.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_order_items")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemId;
    private String itemName;
    private Double itemPrice;
    private Integer itemQuantity;
}
