package com.krowfeather.bank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krowfeather.bank.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
    @Insert("insert into orders (pid,  price) value (#{pid},#{price})")
    void insertEntity(Integer pid, Integer price);

    @Select("select id from orders where pid = #{pid}")
    Integer getOrderId(Integer pid);
}
