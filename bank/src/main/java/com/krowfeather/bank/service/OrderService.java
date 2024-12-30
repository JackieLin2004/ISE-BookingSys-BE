package com.krowfeather.bank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krowfeather.bank.entity.Orders;

public interface OrderService extends IService<Orders> {
    void createOrder(Integer price, Integer pid, Integer cid);
    String pay(int pid, int type);
}
