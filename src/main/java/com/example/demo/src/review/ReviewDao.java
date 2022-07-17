package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ***********************************************************

    // 리뷰 추가
    public int createReview(int baeminOrderIdx,PostReviewReq postReviewReq) {
        String createReviewQuery = "INSERT INTO baemin.ReviewWrite (orderId, score, contents, imgUrl) VALUES (?, ?, ?, ?)";
        Object[] createReviewParams = new Object[] {baeminOrderIdx,postReviewReq.getScore(),postReviewReq.getContents(),postReviewReq.getImgUrl()};
        return this.jdbcTemplate.update(createReviewQuery,createReviewParams);

    }
}
