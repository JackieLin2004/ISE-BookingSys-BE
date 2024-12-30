package com.krowfeather.bookingsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krowfeather.bookingsystem.entity.Proposal;
import com.krowfeather.bookingsystem.entity.VO.ProposalVO;
import com.krowfeather.bookingsystem.entity.VO.Proposal2VO;
import com.krowfeather.bookingsystem.mapper.ProposalMapper;
import com.krowfeather.bookingsystem.service.ProposalService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ProposalServiceImpl extends ServiceImpl<ProposalMapper, Proposal> implements ProposalService {
    private RabbitTemplate rabbitTemplate;
    private final ProposalMapper proposalMapper;

    public ProposalServiceImpl(ProposalMapper proposalMapper, RabbitTemplate rabbitTemplate) {
        this.proposalMapper = proposalMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @Override
    public void generateProposal(Object depart, Object destination, Object cid) throws JsonProcessingException {
        int ticketPrice = 800 + new Random().nextInt(5000-800);
        int totalPrice = (int) (ticketPrice + Math.random() * (100));
        System.out.println("ticket_price:"+ticketPrice);
        System.out.println("total_price:"+totalPrice);
        Integer days = new Random().nextInt(10);
        Proposal proposal = new Proposal();
        proposal.setDepart(depart.toString());
        proposal.setDestination(destination.toString());
        proposal.setTicketPrice(ticketPrice);
        proposal.setTotalPrice(totalPrice);
        proposal.setDays(days);
        proposal.setCid((Integer) cid);
        proposalMapper.insert(proposal);
        ObjectMapper objectMapper = new ObjectMapper();
        ProposalVO proposalVO = Proposal2VO.convert(proposal);
        String proposalJson = objectMapper.writeValueAsString(proposalVO);
        Map<String, Object> data = new HashMap<>();
        data.put("proposal", proposalJson);
        rabbitTemplate.convertAndSend("send_proposal.queue1", data);
    }


    @Override
    public void processAccept(Object pid) {
        Map<String, Object> data = new HashMap<>();
        data.put("pid", pid);
        data.put("cid", proposalMapper.selectById((Serializable) pid).getCid());
        data.put("price", proposalMapper.selectById((Serializable) pid).getTicketPrice());
        System.out.println(data);
        this.rabbitTemplate.convertAndSend("payment_bank.queue1", data);
        this.rabbitTemplate.convertAndSend("payment_customer.queue1", data);
    }

    @Transactional
    @Override
    public void getAllWaitingProposal(Integer id) {
        List<Proposal> proposals = proposalMapper.getAllWaitingProposal(id);
        List<ProposalVO> proposalVOList = new ArrayList<>();
        for (Proposal proposal : proposals) {
            ProposalVO proposalVO = Proposal2VO.convert(proposal);
            proposalVOList.add(proposalVO);
        }
        this.rabbitTemplate.convertAndSend("proposal_get.queue1", proposalVOList);
    }

}
