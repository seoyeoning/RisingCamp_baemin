package com.example.demo.src.review.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReviewReq {

    private int score;
    private String contents;
    private String imgUrl;
}
