package com.krowfeather.bank.entity.pay;

public class WechatPayment implements IPayment {
    @Override
    public void pay() {
        System.out.println("微信支付");
    }
}
