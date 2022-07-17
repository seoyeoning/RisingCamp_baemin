package com.example.demo.src.shop;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.shop.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class ShopProvider {

    private final ShopDao shopDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ShopProvider(ShopDao shopDao, JwtService jwtService) {
        this.shopDao = shopDao;
        this.jwtService = jwtService;
    }
    // **********************************************************

    // 가게 정보 조회
    public GetShopInfoRes getUserShopInfo(int shopIdx) throws BaseException {
        try {
            GetShopInfoRes getShopInfoRes = shopDao.getUserShopInfo(shopIdx);
            return getShopInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
