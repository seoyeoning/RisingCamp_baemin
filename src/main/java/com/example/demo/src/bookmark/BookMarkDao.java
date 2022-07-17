package com.example.demo.src.bookmark;

import com.example.demo.src.bookmark.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class BookMarkDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ******************************************************************

    // 가게 찜 추가
    public int createBookMarkShop(PostBookMarkShopReq postBookMarkShopReq) {
        String createBookMarkShopQuery = "INSERT INTO BookMark (userId, shopId) VALUES (?, ?)";
        Object[] createBookMarkShopParams = new Object[]{postBookMarkShopReq.getUserIdx(),postBookMarkShopReq.getShopIdx()};

        return this.jdbcTemplate.update(createBookMarkShopQuery,createBookMarkShopParams);
    }

}
