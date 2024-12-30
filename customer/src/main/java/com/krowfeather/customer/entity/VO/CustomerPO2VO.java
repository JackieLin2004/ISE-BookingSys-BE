package com.krowfeather.customer.entity.VO;

import com.krowfeather.customer.entity.Customer;

public class CustomerPO2VO {
    public static CustomerVO convert(Customer customerPO) {
        CustomerVO customerVO = new CustomerVO();
        customerVO.setId(customerPO.getId());
        customerVO.setUsername(customerPO.getUsername());
        return customerVO;
    }
}
