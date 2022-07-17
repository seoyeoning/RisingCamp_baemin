package com.example.demo.src.cart.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostCartReq {
    private String deliveryOrPickup;
    private int qty;

}
