package com.krowfeather.bookingsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("proposal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proposal {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String depart;
    private String destination;
    private Integer totalPrice;
    private Integer ticketPrice;
    private Integer days;
    private Integer cid;
    private Integer status;
}
