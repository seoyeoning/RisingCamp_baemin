package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class CartService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CartDao cartDao;
    private final CartProvider cartProvider;
    private final JwtService jwtService;

    @Autowired
    public CartService(CartDao cartDao, CartProvider cartProvider, JwtService jwtService) {
        this.cartDao = cartDao;
        this.cartProvider = cartProvider;
        this.jwtService = jwtService;
    }
    // ********************************************************************

    //장바구니에 메뉴 추가
    public void createCart(int userIdx, int menuIdx, PostCartReq postCartReq) throws BaseException {
            try {
                int result = cartDao.createCart(userIdx, menuIdx, postCartReq);
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }


    }

    //장바구니에서 메뉴 삭제
    public void deleteCartMenu(int cartMenuIdx) throws BaseException{
        try {
            int result = cartDao.deleteCartMenu(cartMenuIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
