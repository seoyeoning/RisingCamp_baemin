package com.example.demo.src.order.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostOrderReq {

    private String spoon;
    private String sideDish;
    private String forOwner;
    private String forRider;
    private int paymentsId;
}
