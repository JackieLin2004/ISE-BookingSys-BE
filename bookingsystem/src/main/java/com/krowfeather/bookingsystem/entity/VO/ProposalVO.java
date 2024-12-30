package com.krowfeather.bookingsystem.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalVO {
    private Integer id;
    private String depart;
    private String destination;
    private Integer totalPrice;
    private Integer ticketPrice;
    private Integer days;
    private Integer cid;
}
