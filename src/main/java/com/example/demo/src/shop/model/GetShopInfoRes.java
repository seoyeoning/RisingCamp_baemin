package com.example.demo.src.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetShopInfoRes {
    private String shopLogoImgUrl;
    private String name;
    private Float shopScoreAvg;
    private int minPrice;
    private String paymentWay;
    private String deliveryTime;
    private String tipRange;
}
