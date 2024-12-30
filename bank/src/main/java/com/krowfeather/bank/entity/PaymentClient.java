package com.krowfeather.bank.entity;

import com.krowfeather.bank.entity.factory.IPaymentFactory;
import com.krowfeather.bank.entity.pay.IPayment;

public class PaymentClient {
    private final IPayment payment;

    public PaymentClient(IPaymentFactory factory) {
        this.payment = factory.createPayment();
    }

    public void pay() {
        payment.pay();
    }
}
