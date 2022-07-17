package com.example.demo.src.bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.bookmark.model.*;
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

public class BookMarkService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookMarkDao bookMarkDao;
    private final BookMarkProvider bookMarkProvider;
    private final JwtService jwtService;

    @Autowired
    public BookMarkService(BookMarkDao bookMarkDao, BookMarkProvider bookMarkProvider, JwtService jwtService) {
        this.bookMarkDao = bookMarkDao;
        this.bookMarkProvider = bookMarkProvider;
        this.jwtService = jwtService;
    }
    // *********************************************************

    //가게 찜
    public void createBookMarkShop(PostBookMarkShopReq postBookMarkShopReq) throws BaseException {
        try{
            int result = bookMarkDao.createBookMarkShop(postBookMarkShopReq);

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
