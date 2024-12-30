package com.krowfeather.bank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer status;
    private Integer price;
    private Integer pid;
    private Integer cid;
}
