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
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class ShopService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ShopDao shopDao;
    private final ShopProvider shopProvider;
    private final JwtService jwtService;

    @Autowired
    public ShopService(ShopDao shopDao, ShopProvider shopProvider, JwtService jwtService) {
        this.shopDao = shopDao;
        this.shopProvider = shopProvider;
        this.jwtService = jwtService;
    }
    // ************************************************************************

    // 가게 추가
    public PostShopRes createShop(PostShopReq postShopReq) throws BaseException {

        try {
            int shopIdx = shopDao.createShop(postShopReq);
            return new PostShopRes(shopIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //가게 삭제
    public void deleteShop(int shopIdx) throws BaseException {
        try {
            int result = shopDao.deleteShop(shopIdx);
            // 실패 validation result=0 일때
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //가게에 메뉴 추가
    public PostMenuRes createMenu(int shopIdx, PostMenuReq postMenuReq) throws BaseException {
        try {
            int menuIdx = shopDao.createMenu(shopIdx,postMenuReq);
            return new PostMenuRes(shopIdx,menuIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //가게의 메뉴 삭제
    public void deleteMenu(int menuIdx) throws BaseException {
        try {
            int result = shopDao.deleteMenu(menuIdx);
            // 실패 validation result=0 일때
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
