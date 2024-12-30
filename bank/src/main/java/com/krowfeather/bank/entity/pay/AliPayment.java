package com.krowfeather.bank.entity.pay;

public class AliPayment implements IPayment {
    @Override
    public void pay() {
        System.out.println("支付宝支付");
    }
}
