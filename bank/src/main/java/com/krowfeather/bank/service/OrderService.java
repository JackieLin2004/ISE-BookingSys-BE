package com.krowfeather.bank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krowfeather.bank.entity.Orders;

public interface OrderService extends IService<Orders> {
    void createOrder(Object pid, Object cid, Object price);
    String pay(int pid, int type);
}
