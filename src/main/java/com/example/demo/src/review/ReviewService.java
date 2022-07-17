package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.review.model.*;
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

public class ReviewService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao =reviewDao;
        this.reviewProvider =reviewProvider;
        this.jwtService = jwtService;
    }
    // *************************************************************

    //리뷰 추가
    public void createReview(int baeminOrderIdx,PostReviewReq postReviewReq) throws BaseException {
        try {
           int result = reviewDao.createReview(baeminOrderIdx,postReviewReq);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
