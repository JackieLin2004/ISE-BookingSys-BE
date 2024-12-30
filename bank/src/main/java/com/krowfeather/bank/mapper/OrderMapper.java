package com.krowfeather.bank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krowfeather.bank.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Insert("insert into `order` (price, pid, cid) value (#{price},#{pid},#{cid})")
    void insertOrder(Integer pid, Integer cid, Integer price);
}
