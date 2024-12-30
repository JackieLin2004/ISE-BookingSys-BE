package com.krowfeather.bank.controller;

import com.krowfeather.bank.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Resource
    OrderService orderService;

    @PostMapping("/pay")
    public String pay(@RequestParam int pid, @RequestParam int type) {
        return orderService.pay(pid, type);
    }
}
