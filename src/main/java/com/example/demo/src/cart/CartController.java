package com.example.demo.src.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.cart.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController

@RequestMapping("/baemin/carts")

public class CartController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CartProvider cartProvider;
    @Autowired
    private final CartService cartService;
    @Autowired
    private final JwtService jwtService;

    public CartController(CartProvider cartProvider, CartService cartService, JwtService jwtService) {
        this.cartProvider = cartProvider;
        this.cartService = cartService;
        this.jwtService = jwtService;
    }
    // *********************************************************

    /**
     * 장바구니에 메뉴 추가 API
     * [POST] /:userIdx/:menuIdx/new-cart
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{menuIdx}/new-cart")
    public BaseResponse<String> createCart(@PathVariable("userIdx") int userIdx, @PathVariable("menuIdx") int menuIdx,@RequestBody PostCartReq postCartReq) {

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            postCartReq = new PostCartReq(postCartReq.getDeliveryOrPickup(), postCartReq.getQty());
            cartService.createCart(userIdx,menuIdx,postCartReq);

            String result = "장바구니에 메뉴가 추가되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 장바구니의 메뉴 삭제 API
     * [DELETE] /:userIdx/:cartMenuIdx
     */
    @ResponseBody
    @DeleteMapping("/{userIdx}/{cartMenuIdx}")
    public BaseResponse<String> deleteCartMenu(@PathVariable("cartMenuIdx") int cartMenuIdx,@PathVariable("userIdx") int userIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            cartService.deleteCartMenu(cartMenuIdx);

            String result = "장바구니에서 메뉴가 삭제되었습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
