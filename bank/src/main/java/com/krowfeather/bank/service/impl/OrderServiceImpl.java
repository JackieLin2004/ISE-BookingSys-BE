package com.krowfeather.bank.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krowfeather.bank.entity.Order;
import com.krowfeather.bank.mapper.OrderMapper;
import com.krowfeather.bank.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService{
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public void createOrder(Object pid, Object cid, Object price) {
        this.orderMapper.insertOrder((Integer) pid,(Integer) cid, (Integer) price);
    }
}
