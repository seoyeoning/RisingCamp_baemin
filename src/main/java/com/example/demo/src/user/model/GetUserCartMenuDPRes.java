package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetUserCartMenuDPRes {
    private int userId;
    private String shopName;
    private String menuName;
    private String menuPrice;
    private int qty;
    private String menuTotalPrice;

}
