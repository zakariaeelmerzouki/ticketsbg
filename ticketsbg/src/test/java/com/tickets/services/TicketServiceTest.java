package com.tickets.services;

import static com.tickets.test.TestUtils.getRandomString;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.Email;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tickets.exceptions.PaymentException;
import com.tickets.exceptions.TicketAlterationException;
import com.tickets.exceptions.TicketCreationException;
import com.tickets.mocks.EmailListener;
import com.tickets.mocks.EmailServiceMock;
import com.tickets.model.Customer;
import com.tickets.model.Discount;
import com.tickets.model.DiscountType;
import com.tickets.model.Firm;
import com.tickets.model.Order;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;
import com.tickets.services.valueobjects.PaymentData;
import com.tickets.services.valueobjects.Seat;
import com.tickets.services.valueobjects.TicketCount;
import com.tickets.services.valueobjects.TicketCountsHolder;
import com.tickets.test.BaseTest;
import com.tickets.utils.GeneralUtils;

public class TicketServiceTest extends BaseTest {

    private static final String TICKET_EMAIL = "test@test.com";

    @Autowired
    private PaymentService paymentService;

    @PostConstruct
    @Override
    public void init() {
        List<Ticket> tickets = ticketService.list(Ticket.class);
        for (Ticket t : tickets) {
            ticketService.delete(t);
        }
        super.init();
    }

    @Autowired
    public TicketService ticketService;

    @Test
    public void generateTicketCodeTest() {
        Run run = new Run();
        run.setRunId(2412);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        run.setTime(cal);
        Route route = new Route();
        getRoute().setId(402);
        run.setRoute(route);
        Firm firm = new Firm();
        firm.setFirmId(17);
        route.setFirm(firm);

        String code = ticketService.generateTicketCode(run);
        Assert.assertNotNull(code);
        Assert.assertTrue(code.replace("-", "").length() % 2 == 0);
    }

    @Test
    @Transactional
    public void createTicketTest() {
        TicketCountsHolder tch = new TicketCountsHolder();
        tch.setRegularTicketsCount(1);
        Ticket ticket = createTicket(tch);
        Assert.assertEquals(ticket.getTotalPrice(), ONE_WAY_PRICE);
    }


    @Test
    @Transactional
    public void createReturnTicketTest() {
        SearchResultEntry entry = formEntry(getRoute());
        SearchResultEntry returnEntry = formEntry(getReturnRoute());
        TicketCountsHolder tch = new TicketCountsHolder();
        tch.setRegularTicketsCount(2);
        try {
            Ticket ticket = ticketService.createTicket(entry, returnEntry, tch, new ArrayList<Seat>(), new ArrayList<Seat>());

            Assert.assertEquals(RETURN_PRICE.multiply(BigDecimal.valueOf(2)), ticket.getTotalPrice());
            Assert.assertEquals(2, ticket.getPassengerDetails().size());
        } catch (TicketCreationException tce) {
            Assert.fail(tce.getMessageKey());
        }
    }


    @Test
    @Transactional
    public void createDiscountTicketTest() {
        TicketCountsHolder tch = createDiscountedTicketCountsHolder();
        tch.setRegularTicketsCount(0);
        Ticket ticket = createTicket(tch);

        // The discount value is equal to the ticket value, so 0 returned.
        Assert.assertEquals(BigDecimal.ZERO, ticket.getTotalPrice());
        Assert.assertEquals(2, ticket.getPassengerDetails().size());
        Assert.assertEquals(2, ticket.getPassengersCount());
    }

    @Test
    @Transactional
    public void alterTicketTest() {
        createTicketTest();
        SearchResultEntry entry = formEntry(getRoute(), 1);
        TicketCountsHolder tch = new TicketCountsHolder();
        tch.setRegularTicketsCount(1);

        Ticket ticket = ticketService.list(Ticket.class).get(0);
        try {
            ticketService.alterTicket(ticket, entry, null, tch, new ArrayList<Seat>(), null);
            // check if the run is actually changed
            Ticket changedTicket = ticketService.get(Ticket.class, ticket.getId());
            Assert.assertTrue(changedTicket.getRun().getRunId() == entry.getRun().getRunId());
        } catch (TicketCreationException ex) {
            Assert.fail(ex.getMessageKey());
        }
    }

    @Test
    @Transactional
    public void calculatePriceTest() {
        SearchResultEntry entry = formEntry(getRoute());
        TicketCountsHolder tch = new TicketCountsHolder();
        tch.setRegularTicketsCount(5);
        BigDecimal price = ticketService.calculatePrice(entry, null, tch);

        Assert.assertEquals(ONE_WAY_PRICE.multiply(BigDecimal.valueOf(5)), price);

        tch = createDiscountedTicketCountsHolder();
        tch.setRegularTicketsCount(2);

        price = ticketService.calculatePrice(entry, null, tch);
        // discounted tickets value should be 0
        Assert.assertEquals(ONE_WAY_PRICE.multiply(BigDecimal.valueOf(2)), price);
    }


    @Test
    @Transactional
    public void timeoutTest() {
        Ticket t = createTicket(new TicketCountsHolder());

        Order order = new Order();
        order.setLanguageCode(new Locale("bg").getLanguage());

        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        tickets.add(t);
        order.setTickets(tickets);

        try {
            PaymentData pd = paymentService.getPaymentData(order, PaymentMethod.E_PAY);
            Assert.assertNotNull(pd);
        } catch (PaymentException ex) {
            ex.printStackTrace();
            Assert.fail();
        }

        t.setCreationTime(GeneralUtils.getPreviousDay());

        ticketService.timeoutUnusedTickets();

        t = getDao().getById(Ticket.class, t.getId());
        Assert.assertTrue(t.isTimeouted());

        try {
            ticketService.clearTimeoutedTickets();

            t = getDao().getById(Ticket.class, t.getId());

            Assert.assertNull("Ticket not deleted", t);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @Transactional
    public void findTicketTest() throws TicketAlterationException {
        Ticket ticket = createTicket(new TicketCountsHolder(), 3);
        Customer customer = new Customer();
        customer.setEmail(TICKET_EMAIL);
        customer.setName(getRandomString(5));
        customer.setContactPhone(getRandomString(5));
        ticket.setCustomerInformation(customer);
        Ticket foundTicket = ticketService.findTicket(ticket.getTicketCode(), TICKET_EMAIL);
        Assert.assertNotNull(foundTicket);
        Assert.assertEquals(ticket.getTicketCode(), foundTicket.getTicketCode());
    }

    @Test(expected=TicketAlterationException.class)
    @Transactional
    public void attemptFindTicketToAlterRightBeforeTravelTest() throws TicketAlterationException {
        // if run right after midnight, wouldn't work, so throwing
        // the exception manually (TODO fix)
        if (GeneralUtils.createCalendar().get(Calendar.HOUR_OF_DAY) < 2) {
            throw new TicketAlterationException();
        }
        setRoute(getRouteService().get(Route.class, getRoute().getId()));
        // won't work on new year, but who would run unit tests on new year? :)
        int currentDayOfYear = GeneralUtils.createCalendar().get(Calendar.DAY_OF_YEAR);
        if (getRoute().getRuns().get(0).getTime().get(Calendar.DAY_OF_YEAR) > currentDayOfYear) {
            getRoute().getRuns().get(0).getTime().set(Calendar.DAY_OF_YEAR, currentDayOfYear);
        }

        Ticket ticket = createTicket(new TicketCountsHolder(), 0);
        Customer customer = new Customer();
        customer.setEmail(TICKET_EMAIL);
        customer.setName(getRandomString(5));
        customer.setContactPhone(getRandomString(5));
        ticket.setCustomerInformation(customer);

        ticketService.findTicket(ticket.getTicketCode(), TICKET_EMAIL);
    }

    @Test
    @Transactional
    public void finalizePurchaseTest() {
        // Not testing with User param instead of paymentCode,
        // because the underlying code is the same, except that
        // the paymentCode variant should send an Email

        EmailServiceMock emailService = new EmailServiceMock();
        emailService.addListener(new EmailListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void emailSent(Email email) {
                try {
                    Field f = Email.class.getDeclaredField("toList");
                    f.setAccessible(true);
                    List<InternetAddress> rcpt = (List<InternetAddress>) f.get(email);
                    Assert.assertEquals(TICKET_EMAIL, rcpt.get(0).getAddress());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Assert.fail(ex.getMessage());
                }
            }
        });

        ((TicketServiceImpl) ticketService).setEmailService(emailService);

        Ticket t = createTicket(new TicketCountsHolder());

        Customer customer = new Customer();
        customer.setEmail(TICKET_EMAIL);
        customer.setName(getRandomString(5));
        customer.setContactPhone(getRandomString(5));
        t.setCustomerInformation(customer);

        String paymentCode = getRandomString(10);
        t.setPaymentCode(paymentCode);
        List<Ticket> tickets = new ArrayList<Ticket>();
        tickets.add(t);
        ticketService.finalizePurchase(tickets, paymentCode);

        Assert.assertTrue(t.isCommitted());
    }

    private static final Integer PURCHASE_ATTEMPTERS = 3;

    @Test
    @Transactional
    public void concurrentPurchaseAttemptTest() {
        SearchResultEntry entry = formEntry(getRoute(), 3);

        getRoute().setSeats(1);

        List<TicketPurchaseAttempter> attempters = new ArrayList<TicketPurchaseAttempter>(
                PURCHASE_ATTEMPTERS);

        for (int i = 0; i < PURCHASE_ATTEMPTERS; i++) {
            TicketPurchaseAttempter attempter = new TicketPurchaseAttempter(entry);
            attempters.add(attempter);
        }

        for (TicketPurchaseAttempter attempter : attempters) {
            attempter.run();
        }

        try {
            for (TicketPurchaseAttempter attempter : attempters) {
                attempter.join();
            }
            System.out.println("VACANT: " + ServiceFunctions.getVacantSeats(entry.getRun(), START_STOP, END_STOP));

            Assert.assertEquals(PURCHASE_ATTEMPTERS.intValue() - 1,
                    purchaseFailures.intValue());

        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Assert.fail();
        } finally {
            // restore the original value
            getRoute().setSeats(51);
        }
    }

    private TicketCountsHolder createDiscountedTicketCountsHolder() {
        TicketCountsHolder tch = new TicketCountsHolder();
        Discount discount = new Discount();
        discount.setValue(ONE_WAY_PRICE);
        discount.setDiscountType(DiscountType.FIXED);
        discount = getDao().persist(discount);
        List<TicketCount> tcList = new ArrayList<TicketCount>();
        TicketCount tc = new TicketCount();
        tc.setDiscount(discount);
        tc.setNumberOfTickets(2);
        tcList.add(tc);
        tch.setTicketCounts(tcList);

        return tch;
    }

    private Ticket createTicket(TicketCountsHolder tch) {
        return createTicket(tch, 0);
    }

    private Ticket createTicket(TicketCountsHolder tch, int runPosition) {
        SearchResultEntry entry = formEntry(getRoute(), runPosition);

        try {
            return ticketService.createTicket(entry, null, tch, new ArrayList<Seat>(), null);
        } catch (TicketCreationException tce) {
            Assert.fail(tce.getMessageKey());
            return null;
        }
    }

    private SearchResultEntry formEntry(Route route) {
        return formEntry(route, 0);
    }

    private SearchResultEntry formEntry(Route route, int runPosition) {
        SearchResultEntry entry = new SearchResultEntry();
        route = getRouteService().get(Route.class, route.getId());
        entry.setRun(getRoute().getRuns().get(runPosition));
        entry.setPrice(getRoute().getPrices().get(0));
        entry.setDepartureTime(entry.getRun().getTime());
        Calendar arrival = entry.getRun().getTime();
        arrival.add(Calendar.MINUTE, entry.getPrice().getEndStop().getTimeToArrival());
        entry.setArrivalTime(arrival);
        entry.setRun(getDao().attach(entry.getRun()));

        return entry;
    }

    private static volatile Integer purchaseFailures = 0;

    private class TicketPurchaseAttempter extends Thread {

        private SearchResultEntry entry;
        public TicketPurchaseAttempter(SearchResultEntry entry) {
            this.entry = entry;
        }

        @Override
        public void run() {
            try {
                ticketService.createTicket(entry, null,
                        new TicketCountsHolder(), new ArrayList<Seat>(), null);

            } catch (TicketCreationException ex) {
                synchronized(purchaseFailures) {
                    purchaseFailures++;
                }
            }
        }
    }
}