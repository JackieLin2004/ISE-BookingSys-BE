package com.krowfeather.bank.entity.factory;

import com.krowfeather.bank.entity.pay.IPayment;
import com.krowfeather.bank.entity.pay.WechatPayment;

public class WechatFactory implements IPaymentFactory {
    @Override
    public IPayment createPayment() {
        return new WechatPayment();
    }
}
