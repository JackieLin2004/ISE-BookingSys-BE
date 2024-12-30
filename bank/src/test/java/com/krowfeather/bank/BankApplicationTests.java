package com.krowfeather.bank;

import com.krowfeather.bank.service.OrderService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankApplicationTests {

    @Resource
    OrderService orderService;

    @Test
    void contextLoads() {
        orderService.pay(1, 0);
    }

}
