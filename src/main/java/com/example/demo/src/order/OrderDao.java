package com.example.demo.src.order;

import com.example.demo.src.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // **********************************************************

    // 주문 추가
    public int createOrder(int cartIdx,PostOrderReq postOrderReq) {
        String createOrderQuery = "INSERT INTO baemin.BaeminOrder (cartId, spoon, sideDish, forOwner, forRider, paymentId) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] createOrderParams = new Object[]{cartIdx,postOrderReq.getSpoon(),postOrderReq.getSideDish(),
        postOrderReq.getForOwner(),postOrderReq.getForRider(),postOrderReq.getPaymentsId()};

        return this.jdbcTemplate.update(createOrderQuery,createOrderParams);
        /**
         * default값이 많을때 프론트에서부터 기본값 가져오기!!!!!!!
         */
    }

}

