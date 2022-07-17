package com.example.demo.src.shop.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostShopReq {
    private int categoryIdx;
    private String shopName;
    private String address;
    private String openCloseTime;
    private String holiDay;
    private String phoneNumber;
    private String introduce;
    private String introduceImageUrl;
    private String shopInfo;
    private String minPrice;
    private String deliveryArea;
    // private String deliveryTime; (회원 주소마다 다름)
    private String reviewNotice;
    private String tipRange;
    //private String pickUp; default Y임 나중에 처리
    //private String baemin1; default Y임 나중에 처리
}
