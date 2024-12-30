package com.krowfeather.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krowfeather.customer.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    @Select("select * from customer where username = #{username}")
    Customer getByUsername(String username);
}
