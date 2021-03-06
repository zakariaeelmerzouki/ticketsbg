package com.tickets.controllers.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.faces.model.SelectItem;

import com.tickets.controllers.valueobjects.Row;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.SearchResultEntry;
import com.tickets.model.SeatSettings;
import com.tickets.model.Vehicle;
import com.tickets.services.ServiceFunctions;
import com.tickets.services.valueobjects.Seat;
import com.tickets.services.valueobjects.TicketCount;
import com.tickets.services.valueobjects.TicketCountsHolder;

public class SeatHandler {

    private Run run;
    private Route route;
    private Price price;
    private Integer[] used;

    private Vehicle vehicle;

    private List<Row> rows;
    private List<SelectItem> seatSelectItems;
    private List<Seat> selectedSeats = new ArrayList<Seat>();

    private int firstSeatUpstairs;

    private TicketCountsHolder ticketCountsHolder;

    private Queue<Seat> selectionQueue = new LinkedBlockingQueue<Seat>();

    public SeatHandler(Route route) {
        this.route = route;
        refreshRows(route.getSeatSettings(), route.getSeats());
    }

    public SeatHandler(Vehicle vehicle) {
        this.vehicle = vehicle;
        refreshRows(vehicle.getSeatSettings(), vehicle.getSeats());
    }

    public SeatHandler(SearchResultEntry entry, TicketCountsHolder ticketCountsHolder) {
        this(entry);
        this.ticketCountsHolder = ticketCountsHolder;
    }

    public SeatHandler(SearchResultEntry entry) {
        this.run = entry.getRun();
        this.route = run.getRoute();
        this.price = entry.getPrice();
        initUsedSeats();
        refreshRows(route.getSeatSettings(), route.getSeats());
    }


    public void refreshRows() {
        if (route != null) {
            refreshRows(route.getSeatSettings(), route.getSeats());
        }

        if (vehicle != null) {
            refreshRows(vehicle.getSeatSettings(), vehicle.getSeats());
        }
    }

    public void refreshRows(SeatSettings seatSettings, int seats) {
        firstSeatUpstairs = seatSettings.getNumberOfSeatsDownstairs() + 1;
        int skippedSeats = 0;
        int rowCount = seats / 4;

        rows = new ArrayList<Row>(rowCount);
        List<Integer> missingSeats = new ArrayList<Integer>();
        missingSeats.addAll(seatSettings.getMissingSeats());
        //sorting so that binary search can be performed later
        Collections.sort(missingSeats);

        for (int i = 0; i < rowCount; i ++) {
            Row row = new Row();
            boolean lastRow = i == rowCount - 1;
            int rowId = i + 1;
            if (!lastRow || seatSettings.isLastRowHasFourSeats()) {
                if (seatSettings.isStartRight()) {

                    skippedSeats += getSkippedSeat(rowId, 1, missingSeats);
                    row.setFourth(new Seat(i * 4 + 1 - skippedSeats, i * 4 + 1));

                    skippedSeats += getSkippedSeat(rowId, 2, missingSeats);
                    row.setThird(new Seat(i * 4 + 2 - skippedSeats, i * 4 + 2));

                    skippedSeats += getSkippedSeat(rowId, 3, missingSeats);
                    row.setSecond(new Seat(i * 4 + 3 - skippedSeats, i * 4 + 3));

                    skippedSeats += getSkippedSeat(rowId, 4, missingSeats);
                    row.setFirst(new Seat(i * 4 + 4 - skippedSeats, i * 4 + 4));

                    row.setId(rowId);
                } else {
                    skippedSeats += getSkippedSeat(rowId, 1, missingSeats);
                    row.setFirst(new Seat(i * 4 + 1 - skippedSeats, i * 4 + 1));

                    skippedSeats += getSkippedSeat(rowId, 2, missingSeats);
                    row.setSecond(new Seat(i * 4 + 2 - skippedSeats, i * 4 + 2));

                    skippedSeats += getSkippedSeat(rowId, 3, missingSeats);
                    row.setThird(new Seat(i * 4 + 3 - skippedSeats, i * 4 + 3));

                    skippedSeats += getSkippedSeat(rowId, 4, missingSeats);
                    row.setFourth(new Seat(i * 4 + 4 - skippedSeats, i * 4 + 4));
                    row.setId(rowId);
                }
            } else {
                 if (seatSettings.isStartRight()) {

                     skippedSeats += getSkippedSeat(rowId, 1, missingSeats);
                     row.setFourth(new Seat(i * 4 + 1 - skippedSeats, i * 4 + 1));

                     skippedSeats += getSkippedSeat(rowId, 2, missingSeats);
                     row.setThird(new Seat(i * 4 + 2 - skippedSeats, i * 4 + 2));

                     row.setMiddleSeat(new Seat(i * 4 + 3 - skippedSeats, i * 4 + 3));

                     skippedSeats += getSkippedSeat(rowId, 3, missingSeats);
                     row.setSecond(new Seat(i * 4 + 4 - skippedSeats, i * 4 + 4));

                     skippedSeats += getSkippedSeat(rowId, 4, missingSeats);
                     row.setFirst(new Seat(i * 4 + 5 - skippedSeats, i * 4 + 5));

                     row.setId(rowId);
                 } else {

                     skippedSeats += getSkippedSeat(rowId, 1, missingSeats);
                     row.setFirst(new Seat(i * 4 + 1 - skippedSeats, i * 4 + 1));

                     skippedSeats += getSkippedSeat(rowId, 2, missingSeats);
                     row.setSecond(new Seat(i * 4 + 2 - skippedSeats, i * 4 + 2));

                     row.setMiddleSeat(new Seat(i * 4 + 3 - skippedSeats, i * 4 + 3));

                     skippedSeats += getSkippedSeat(rowId, 3, missingSeats);
                     row.setThird(new Seat(i * 4 + 4 - skippedSeats, i * 4 + 4));

                     skippedSeats += getSkippedSeat(rowId, 4, missingSeats);
                     row.setFourth(new Seat(i * 4 + 5 - skippedSeats, i * 4 + 5));

                     row.setId(rowId);
                 }
            }

            // Compute vacancies only when purchasing a ticket for a specific run
            if (run != null) {
                row.getFirst().setVacant(isVacant(row.getFirst().getNumber()));
                row.getSecond().setVacant(isVacant(row.getSecond().getNumber()));
                row.getThird().setVacant(isVacant(row.getThird().getNumber()));
                row.getFourth().setVacant(isVacant(row.getFourth().getNumber()));
                if (row.getMiddleSeat() != null) {
                    row.getMiddleSeat().setVacant(isVacant(row.getMiddleSeat().getNumber()));
                }
            }

            // If the bus is a double-decker, and the
            // current row is the first upstairs add a separation row
            if (seatSettings.isDoubleDecker() && (row.getFirst().getNumber() == firstSeatUpstairs ||
                    row.getFourth().getNumber() == firstSeatUpstairs)) {
                Row separatorRow = new Row();
                separatorRow.setSeparator(true);
                rows.add(separatorRow);
            }

            // If rows are needed that are not initially calculated
            if (seats - rowCount * 4 + skippedSeats >= 4) {
                rowCount ++;
            }

            rows.add(row);
        }

        seatSelectItems = new ArrayList<SelectItem>(seats);

        for (Row row : rows) {
            // skip the iteration in case this is the separation 'row'
            // between first and second floor
            if (row.isSeparator()) {
                continue;
            }
            if (seatSettings.isStartRight()) {
                SelectItem si = new SelectItem(row.getFourth(), "" + row.getFourth().getNumber());
                si.setDisabled(!row.getFourth().isVacant() && run != null);
                handleMissingSeat(row.getFourth().getNumber(), row.getId(), 1, missingSeats, run, si);
                seatSelectItems.add(si);

                si = new SelectItem(row.getThird(), "" + row.getThird().getNumber());
                si.setDisabled(!row.getThird().isVacant() && run != null);
                handleMissingSeat(row.getThird().getNumber(), row.getId(), 2, missingSeats, run, si);
                seatSelectItems.add(si);

                // The middle seat is not tried for being missing,
                // because of a separate setting
                if (row.getMiddleSeat() != null) {
                    si = new SelectItem(row.getMiddleSeat(), "" + row.getMiddleSeat().getNumber());
                    si.setDisabled(!row.getMiddleSeat().isVacant() && run != null);
                    seatSelectItems.add(si);
                }

                si = new SelectItem(row.getSecond(), "" + row.getSecond().getNumber());
                si.setDisabled(!row.getSecond().isVacant() && run != null);
                handleMissingSeat(row.getSecond().getNumber(), row.getId(), 3, missingSeats, run, si);
                seatSelectItems.add(si);

                si = new SelectItem(row.getFirst(), "" + row.getFirst().getNumber());
                si.setDisabled(!row.getFirst().isVacant() && run != null);
                handleMissingSeat(row.getFirst().getNumber(), row.getId(), 4, missingSeats, run, si);
                seatSelectItems.add(si);

            } else {
                SelectItem si = new SelectItem(row.getFirst(), "" + row.getFirst().getNumber());
                si.setDisabled(!row.getFirst().isVacant() && run != null);
                handleMissingSeat(row.getFirst().getNumber(), row.getId(), 1, missingSeats, run, si);
                seatSelectItems.add(si);

                si = new SelectItem(row.getSecond(), "" + row.getSecond().getNumber());
                si.setDisabled(!row.getSecond().isVacant() && run != null);
                handleMissingSeat(row.getSecond().getNumber(), row.getId(), 2, missingSeats, run, si);
                seatSelectItems.add(si);

                // The middle seat is not tried for being missing,
                // because of a separate setting
                if (row.getMiddleSeat() != null) {
                    si = new SelectItem(row.getMiddleSeat(), "" + row.getMiddleSeat().getNumber());
                    si.setDisabled(!row.getMiddleSeat().isVacant() && run != null);
                    seatSelectItems.add(si);
                }

                si = new SelectItem(row.getThird(), "" + row.getThird().getNumber());
                si.setDisabled(!row.getThird().isVacant() && run != null);
                handleMissingSeat(row.getThird().getNumber(), row.getId(), 3, missingSeats, run, si);
                seatSelectItems.add(si);

                si = new SelectItem(row.getFourth(), "" + row.getFourth().getNumber());
                si.setDisabled(!row.getFourth().isVacant() && run != null);
                handleMissingSeat(row.getFourth().getNumber(), row.getId(), 4, missingSeats, run, si);
                seatSelectItems.add(si);
            }
        }
    }

    private int getSkippedSeat(int row, int numberInRow, List<Integer> missingSeats) {
        int seatId = (row - 1)* 4 + numberInRow;
        if (Collections.binarySearch(missingSeats, seatId) > -1) {
            return 1;
        }
        return 0;
    }

    private void handleMissingSeat(int number, int row, int numberInRow, List<Integer> missingSeats, Run run, SelectItem si) {
        int seatId = (row - 1) * 4 + numberInRow;
        // If the current row is missing, made it disabled in
        // the public part, and checked in the admin part
        if (Collections.binarySearch(missingSeats, seatId) > -1) {
            if (run == null) {
                selectedSeats.add(new Seat(number, seatId));
                si.setLabel("");
            } else {
                si.setDisabled(true);
                si.setLabel("");
            }
        }
    }

    private boolean isVacant(int number) {
        if (Arrays.binarySearch(used, number) < 0) {
            return true;
        }
        return false;
    }

    public void initUsedSeats() {

        List<Integer> u = ServiceFunctions.getUsedSeats(run, price
                .getStartStop().getName(), price.getEndStop().getName());
        used = u.toArray(new Integer[u.size()]);
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public List<SelectItem> getSeatSelectItems() {
        return seatSelectItems;
    }

    public void setSeatSelectItems(List<SelectItem> seatSelectItems) {
        this.seatSelectItems = seatSelectItems;
    }

    public List<Seat> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(List<Seat> selectedSeats) {
        if (run != null) {
            Seat newSelection = null;
            for (Seat seat : selectedSeats) {
                if (!this.selectedSeats.contains(seat)) {
                    newSelection = seat;
                    break;
                }
            }
            // in case of other events just re-setting the already existing value
            if (newSelection == null) {
                this.selectedSeats = selectedSeats;
                return;
            }

            selectionQueue.add(newSelection);

            if (selectedSeats.size() > getTotalNumberOfTickets()) {
                selectedSeats.remove(selectionQueue.poll());
            }
        }

        this.selectedSeats = selectedSeats;
        if (run == null) {
            updateMissingSeats(route != null ? route.getSeatSettings() : vehicle.getSeatSettings());
        }
    }

    private void updateMissingSeats(SeatSettings seatSettings) {
        List<Integer> missing = new ArrayList<Integer>();
        for (Seat seat : selectedSeats) {
            missing.add(seat.getId());
        }
        seatSettings.setMissingSeats(missing);
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public int getFirstSeatUpstairs() {
        return firstSeatUpstairs;
    }

    public void setFirstSeatUpstairs(int firstSeatUpstairs) {
        this.firstSeatUpstairs = firstSeatUpstairs;
    }

    public TicketCountsHolder getTicketCountsHolder() {
        return ticketCountsHolder;
    }

    public void setTicketCountsHolder(TicketCountsHolder ticketCountsHolder) {
        this.ticketCountsHolder = ticketCountsHolder;
    }

    public Integer[] getUsed() {
        return used;
    }

    public void setUsed(Integer[] used) {
        this.used = used;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getTotalNumberOfTickets() {
        if (run == null) {
            return 0;
        }

        if (ticketCountsHolder == null) {
            return 0;
        }

        int total = ticketCountsHolder.getRegularTicketsCount();
        for (TicketCount tc : ticketCountsHolder.getTicketCounts()) {
            total += tc.getNumberOfTickets();
        }

        return total;
    }
}
