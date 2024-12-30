package com.krowfeather.bank.controller;

import com.krowfeather.bank.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Resource
    OrderService orderService;

    @PostMapping("/pay/{pid}/{type}")
    public String pay(@PathVariable Integer pid,@PathVariable Integer type) {
        System.out.println("pid : " + pid+" type : "+type);
        return orderService.pay(pid, type);
    }
}
