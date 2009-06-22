package com.tickets.services;

import com.tickets.model.Discount;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.Ticket;

public interface TicketService extends Service<Ticket> {

    Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry);

    Ticket createTicket(SearchResultEntry selectedEntry,
            SearchResultEntry selectedReturnEntry, Discount discount);
}
