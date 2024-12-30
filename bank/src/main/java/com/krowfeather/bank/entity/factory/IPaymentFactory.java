package com.krowfeather.bank.entity.factory;

import com.krowfeather.bank.entity.pay.IPayment;

public interface IPaymentFactory {
    IPayment createPayment();
}
