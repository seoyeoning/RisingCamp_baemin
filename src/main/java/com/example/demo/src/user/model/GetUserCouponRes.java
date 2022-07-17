package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetUserCouponRes {
    private int couponIdx;
    private String couponName;
    private String contents;
    private int minPrice;
    private String startDate;
    private String finishDate;

}
