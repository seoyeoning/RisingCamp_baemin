package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetUserOrdersDPRes {
    private int userId;
    private String orderDate;
    private String orderDay;
    private String orderStatus;
    private String menuName;
    private String orderMenuInfo;

}
