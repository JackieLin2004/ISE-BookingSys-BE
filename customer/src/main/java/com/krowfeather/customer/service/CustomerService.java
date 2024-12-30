package com.krowfeather.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krowfeather.customer.entity.Customer;

public interface CustomerService extends IService<Customer> {
    Customer getByUsername(String username);

    void getProposal(Integer id, String depart, String destination);

    void withdraw(Integer pid);

    void accept(Integer pid);

    String allWaitingProposal(Integer id);

    String allWithdrawedProposal(Integer id);

    String allAcceptedProposal(Integer id);
}
