package com.krowfeather.bookingsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.krowfeather.bookingsystem.entity.Proposal;

public interface ProposalService extends IService<Proposal> {
    void generateProposal(Object depart, Object destination, Object cid) throws JsonProcessingException;

    void processAccept(Object pid);

    void getAllWaitingProposal(Integer id);

    void processWithdraw(Integer pid);
}
