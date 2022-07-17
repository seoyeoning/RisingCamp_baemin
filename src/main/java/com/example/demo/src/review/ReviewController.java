package com.example.demo.src.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController

@RequestMapping("/baemin/reviews")

public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    //***************************************************************************

    /**
     * 가게 리뷰 추가
     * /:userIdx/:shopIdx/:baeminOrderIdx/new-review-shop
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{baeminOrderIdx}/new-review-shop")
    public BaseResponse<String> createReview(@PathVariable("userIdx") int userIdx,@PathVariable("baeminOrderIdx") int baeminOrderIdx,@RequestBody PostReviewReq postReviewReq ) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            postReviewReq = new PostReviewReq(postReviewReq.getScore(),postReviewReq.getContents(),postReviewReq.getImgUrl());
            reviewService.createReview(baeminOrderIdx,postReviewReq);

            String result = "리뷰작성이 완료되었습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
