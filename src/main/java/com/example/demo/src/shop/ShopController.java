package com.example.demo.src.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.shop.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/baemin/shops")

public class ShopController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ShopProvider shopProvider;
    @Autowired
    private final ShopService shopService;
    @Autowired
    private final JwtService jwtService;

    public ShopController(ShopProvider shopProvider, ShopService shopService, JwtService jwtService) {
        this.shopProvider = shopProvider;
        this.shopService = shopService;
        this.jwtService = jwtService;
    }

    // *****************************************************************

    /**
     * 가게 추가 API
     * [POST] /new-shop
     */
    @ResponseBody
    @PostMapping("/new-shop")
    public BaseResponse<PostShopRes> createShop(@RequestBody PostShopReq postShopReq) {
        try {
            PostShopRes postShopRes = shopService.createShop(postShopReq);
            return new BaseResponse<>(postShopRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 삭제 API
     * [PATCH] /:shopIdx
     */
    @ResponseBody
    @PatchMapping("/{shopIdx}")
    public BaseResponse<String> deleteShop(@PathVariable("shopIdx") int shopIdx) {
        try {
            shopService.deleteShop(shopIdx);
            String result = "가게가 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 메뉴 추가 API
     * [POST] /:shopIdx/new-menu
     */
    @ResponseBody
    @PostMapping("/{shopIdx}/new-menu")
    public BaseResponse<PostMenuRes> createMenu(@PathVariable("shopIdx") int shopIdx, @RequestBody PostMenuReq postMenuReq) {
        try {
            PostMenuRes postMenuRes = shopService.createMenu(shopIdx,postMenuReq);
            return new BaseResponse<>(postMenuRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 메뉴 삭제 API
     * [PATCH] /:shopIdx/:menuIdx
     */
    @ResponseBody
    @PatchMapping("/menus/{menuIdx}")
    public BaseResponse<String> deleteMenu(@PathVariable("menuIdx") int menuIdx) {
        try {
            shopService.deleteMenu(menuIdx);
            String result = "메뉴가 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 정보 조회 API
     * [GET] /:shopIdx/:userIdx
     */
    @ResponseBody
    @GetMapping("/{shopIdx}/{userIdx}")
    public BaseResponse<GetShopInfoRes>  getUserShopInfo(@PathVariable("shopIdx") int shopIdx,@PathVariable("userIdx") int userIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetShopInfoRes getShopInfoRes = shopProvider.getUserShopInfo(shopIdx);
            return new BaseResponse<>(getShopInfoRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
