package com.krowfeather.bookingsystem.entity.VO;

import com.krowfeather.bookingsystem.entity.Proposal;

public class Proposal2VO {
    public static ProposalVO convert(Proposal proposal) {
        ProposalVO proposalVO1 = new ProposalVO();
        proposalVO1.setId(proposal.getId());
        proposalVO1.setDepart(proposal.getDepart());
        proposalVO1.setDestination(proposal.getDestination());
        proposalVO1.setDays(proposal.getDays());
        proposalVO1.setTotalPrice(proposal.getTotalPrice());
        proposalVO1.setTicketPrice(proposal.getTicketPrice());
        proposalVO1.setCid(proposal.getCid());
        return proposalVO1;
    }
}
