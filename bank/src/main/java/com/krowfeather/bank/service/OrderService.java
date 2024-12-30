package com.krowfeather.bank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krowfeather.bank.entity.Order;

public interface OrderService extends IService<Order> {
    void createOrder(Object pid, Object cid, Object price);
}
