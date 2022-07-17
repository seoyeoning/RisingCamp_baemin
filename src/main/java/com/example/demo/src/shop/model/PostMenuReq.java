package com.example.demo.src.shop.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostMenuReq {
    private  int shopIdx;
    private String menuName;
    private String menuInfo;
    private int price;
}
