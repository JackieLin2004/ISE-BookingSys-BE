package com.krowfeather.bookingsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.krowfeather.bookingsystem.entity.Proposal;

public interface ProposalService extends IService<Proposal> {
    void generateProposal(Object depart, Object destination, Object cid) throws JsonProcessingException;

    void getAllWaitingProposal(Integer id);

    void processWithdraw(Integer pid);

    void processAccept(Integer pid);

    void getAllWithdrawProposal(Integer id);

    void getAllAcceptProposal(Integer id);

}
