package com.example.demo.src.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController

@RequestMapping("/baemin/orders")

public class OrderController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OrderProvider orderProvider;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final JwtService jwtService;

    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService) {
        this.orderProvider =orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }
    // ****************************************************

    /**
     * 주문 추가 API
     * [POST] /:cartIdx/:userIdx/new-order
     */
    @ResponseBody
    @PostMapping("/{cartIdx}/{userIdx}/new-order")
    public BaseResponse<String> createOrder(@PathVariable("cartIdx") int cartIdx,@PathVariable("userIdx") int userIdx,@RequestBody PostOrderReq postOrderReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            postOrderReq = new PostOrderReq(postOrderReq.getSpoon(),postOrderReq.getSideDish(),postOrderReq.getForOwner(),
                    postOrderReq.getForRider(),postOrderReq.getPaymentsId());

            orderService.createOrder(cartIdx,postOrderReq);

            String result = "주문이 완료되었습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * dao에서 user랑 연결해서 대표 주소도 뽑아야함
     */


}
