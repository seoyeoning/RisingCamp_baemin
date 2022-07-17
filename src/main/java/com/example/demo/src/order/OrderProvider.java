package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class OrderProvider {

    private final OrderDao orderDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OrderProvider( OrderDao orderDao, JwtService jwtService) {
        this.orderDao = orderDao;
        this.jwtService = jwtService;
    }
    // *******************************************************************

}
