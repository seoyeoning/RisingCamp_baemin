package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetUserCartPriceDPRes {
    private int userId;
    private String deliveryOrPickup;
    private String totalOrderPrice;

}
