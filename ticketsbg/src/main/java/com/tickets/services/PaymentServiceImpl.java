package com.tickets.services;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.constants.Settings;
import com.tickets.exceptions.PaymentException;
import com.tickets.model.Ticket;
import com.tickets.services.valueobjects.PaymentData;
import com.tickets.utils.GeneralUtils;
import com.tickets.utils.SecurityUtils;
import com.tickets.utils.base64.Base64Encoder;

@Service("paymentService")
public class PaymentServiceImpl extends BaseService implements PaymentService {

    private static final String E_PAY_DATA_PATTERN = "MIN={0}\nINVOICE={1}\nAMOUNT={2}\nEXP_TIME={3}\nDESCR={4}";

    @Override
    public PaymentData getPaymentData(List<Ticket> tickets)
            throws PaymentException {
        String secret = Settings.getValue("epay.secret");
        String min = Settings.getValue("epay.min");
        String sum = getTotalPrice(tickets).toString();
        String expiryDate = new SimpleDateFormat("dd.MM.yyyy")
                .format(GeneralUtils.createEmptyCalendar().getTime());

        String description = "";

        for (Ticket ticket : tickets) {
            description += ticket.getStartStop() + " - " + ticket.getEndStop()
                    + " : " + ticket.getTicketCode() + "\n";
        }

        String orderId = "";
        String delim = "";
        for (Ticket ticket : tickets) {
            orderId += delim + ticket.getId();
            delim = "-";
        }
        orderId = addDummyDigits(orderId);

        String data = MessageFormat.format(E_PAY_DATA_PATTERN, min, orderId,
                sum, expiryDate, description);

        try {
            PaymentData paymentData = new PaymentData();
            paymentData.setEncoded(Base64Encoder
                    .toBase64String(data.getBytes()));
            paymentData.setChecksum(SecurityUtils.hmac(
                    paymentData.getEncoded(), secret));

            return paymentData;
        } catch (Exception ex) {
            throw new PaymentException(ex);
        }
    }

    private String addDummyDigits(String orderId) {
        int prefix = (int) (Math.random() * 88 + 10);
        int suffix = (int) (Math.random() * 88 + 10);

        return prefix + orderId + suffix;
    }

    private String stripDummyDigits(String orderId) {
        String withoutPrefix = orderId.substring(2);
        String original = withoutPrefix.substring(withoutPrefix.length() - 2);

        return original;
    }

    @Override
    public BigDecimal getTotalPrice(List<Ticket> tickets) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Ticket ticket : tickets) {
            if (!ticket.isAltered()) {
                sum = sum.add(ticket.getTotalPrice());
            } else {
                sum = sum.add(ticket.getAlterationPriceDifference());
            }
        }

        return sum;
    }

}
