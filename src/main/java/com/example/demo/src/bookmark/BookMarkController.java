package com.example.demo.src.bookmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.bookmark.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController

@RequestMapping("/baemin/bookMarks")

public class BookMarkController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BookMarkProvider bookMarkProvider;
    @Autowired
    private final BookMarkService bookMarkService;
    @Autowired
    private final JwtService jwtService;

    public BookMarkController(BookMarkProvider bookMarkProvider, BookMarkService bookMarkService, JwtService jwtService) {
        this.bookMarkProvider =bookMarkProvider;
        this.bookMarkService = bookMarkService;
        this.jwtService = jwtService;
    }
    // ************************************************************

    /**
     * 배달/포장 가게(shop) 찜 추가
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{shopIdx}/new-bookMark-shop")
    public BaseResponse<String> createBookMarkShop(@PathVariable("userIdx") int userIdx,@PathVariable("shopIdx") int shopIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostBookMarkShopReq postBookMarkShopReq = new PostBookMarkShopReq(userIdx,shopIdx);
            bookMarkService.createBookMarkShop(postBookMarkShopReq);

            String result = "찜에 추가되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}


