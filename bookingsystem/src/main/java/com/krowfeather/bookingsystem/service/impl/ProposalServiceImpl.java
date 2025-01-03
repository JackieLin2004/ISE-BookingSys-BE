package com.krowfeather.bookingsystem.service.impl;

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

    @Override
    public void processWithdraw(Integer pid) {
        this.proposalMapper.updateStatus(pid, 2);
        Map<String, Object> data = new HashMap<>();
        ProposalVO proposalVO = Proposal2VO.convert(this.proposalMapper.selectById(pid));
        data.put("cid", proposalVO.getCid());
        data.put("status","ok");
        this.rabbitTemplate.convertAndSend("withdraw_notify.queue", data);
    }

    @Override
    public void processAccept(Integer pid) {
        this.proposalMapper.updateStatus(pid, 1);
        Map<String, Object> data = new HashMap<>();
        ProposalVO proposalVO = Proposal2VO.convert(this.proposalMapper.selectById(pid));
        data.put("cid", proposalVO.getCid());
        data.put("status","ok");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("price", proposalVO.getTicketPrice());
        data1.put("pid", proposalVO.getId());
        data1.put("cid", proposalVO.getCid());
        this.rabbitTemplate.convertAndSend("accept_notify.queue", data);
        this.rabbitTemplate.convertAndSend("pay_bank.queue", data1);
    }

    @Override
    public void getAllWithdrawProposal(Integer id) {
        List<Proposal> proposals = proposalMapper.getAllWithdrawProposal(id);
        List<ProposalVO> proposalVOList = new ArrayList<>();
        for (Proposal proposal : proposals) {
            ProposalVO proposalVO = Proposal2VO.convert(proposal);
            proposalVOList.add(proposalVO);
        }
        this.rabbitTemplate.convertAndSend("proposal_get_withdraw.queue1", proposalVOList);
    }

    @Override
    public void getAllAcceptProposal(Integer id) {
        List<Proposal> proposals = proposalMapper.getAllAcceptProposal(id);
        List<ProposalVO> proposalVOList = new ArrayList<>();
        for (Proposal proposal : proposals) {
            ProposalVO proposalVO = Proposal2VO.convert(proposal);
            proposalVOList.add(proposalVO);
        }
        this.rabbitTemplate.convertAndSend("proposal_get_accept.queue1", proposalVOList);
    }

    @Override
    public void processComplete(Integer pid, Integer orderId) {
        this.proposalMapper.updatePrepared(pid);
        Integer cid = this.proposalMapper.getCid(pid);
        Map<String, Object> data = new HashMap<>();
        data.put("pid", pid);
        data.put("cid", cid);
        data.put("orderId", orderId);
        this.rabbitTemplate.convertAndSend("proposal_order_complete.queue", data);
    }

    @Override
    public void processPayment(Integer pid) {
        this.proposalMapper.updateStatus(pid, 3);
        Map<String, Object> data = new HashMap<>();
        Integer cid = this.proposalMapper.getCid(pid);
        data.put("cid", cid);
        data.put("status","ok");
        this.rabbitTemplate.convertAndSend("payment_notify.queue", data);
    }

    @Override
    public void getAllPaidProposal(Integer id) {
        List<Proposal> proposals = proposalMapper.getAllPaidProposal(id);
        List<ProposalVO> proposalVOList = new ArrayList<>();
        for (Proposal proposal : proposals) {
            ProposalVO proposalVO = Proposal2VO.convert(proposal);
            proposalVOList.add(proposalVO);
        }
        this.rabbitTemplate.convertAndSend("proposal_get_paid.queue1", proposalVOList);
    }

}
