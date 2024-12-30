package com.krowfeather.bank.entity.factory;

import com.krowfeather.bank.entity.pay.AliPayment;
import com.krowfeather.bank.entity.pay.IPayment;

public class AliFactory implements IPaymentFactory {
    @Override
    public IPayment createPayment() {
        return new AliPayment();
    }
}
