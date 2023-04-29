package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Payment payment = new Payment();
        int price = reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour();
        if(amountSent<price){
            throw new Exception("Insufficient Amount");
        }
        if(mode.equals(PaymentMode.CARD.toString()) || mode.equals(PaymentMode.CASH.toString()) || mode.equals(PaymentMode.UPI.toString())){
            payment.setPaymentMode(PaymentMode.valueOf(mode));
            payment.setPaymentCompleted(true);
            payment.setReservation(reservation);
            paymentRepository2.save(payment);
            return payment;
        }
        else{
            throw new Exception("Payment mode not detected");
        }
    }
}
