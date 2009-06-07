package com.tickets.controllers;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.ListDataModel;

import org.richfaces.model.selection.SimpleSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.model.RunPriceHolder;
import com.tickets.services.SearchService;

@Controller("searchController")
@Scope("conversation.access")
public class SearchController extends BaseController {

    @Autowired
    private SearchService searchService;

    private static final String TWO_WAY = "twoWay";

    private String fromStop;
    private String toStop;

    private int fromHour = 0;
    private int toHour = 24;
    private Date date;
    private boolean timeForDeparture = true;

    private int returnFromHour = 0;
    private int returnToHour = 24;
    private Date returnDate;
    private boolean returnTimeForDeparture = true;

    private ListDataModel resultsModel;
    private ListDataModel returnResultsModel;

    private List<String> stopNames;
    private Integer[] hoursFrom;
    private Integer[] hoursTo;
    private String travelType = TWO_WAY;

    private RunPriceHolder selectedRun;
    private SimpleSelection selection;
    private Long selectedRowId;

    @Action
    public String search() {

        List<RunPriceHolder> result = searchService.search(fromStop, toStop, date,
                fromHour, toHour, timeForDeparture);

        resultsModel = new ListDataModel(result);

        if (travelType.equals(TWO_WAY)) {
            List<RunPriceHolder> returnResult = searchService.search(toStop, fromStop,
                    returnDate, returnFromHour, returnToHour,
                    returnTimeForDeparture);

            returnResultsModel = new ListDataModel(returnResult);
        }

        return "searchResults";
    }

    public String toSearchScreen() {
        return "searchScreen";
    }

    @PostConstruct
    public void init() {
        stopNames = searchService.listAllStops();
        hoursFrom = new Integer[24];
        for (int i = 0; i < hoursFrom.length; i++) {
            hoursFrom[i] = i;
        }
        hoursTo = new Integer[24];
        for (int i = 0; i < hoursTo.length; i++) {
            hoursTo[i] = i + 1;
        }
    }

    @SuppressWarnings("unchecked")
    public void rowSelectionChanged() {
        Integer selectedId = (Integer) selection.getKeys().next();
        selectedRowId = new Long(selectedId);
        selectedRun = ((List<RunPriceHolder>) resultsModel.getWrappedData()).get(selectedId);
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getToStop() {
        return toStop;
    }

    public void setToStop(String toStop) {
        this.toStop = toStop;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ListDataModel getResultsModel() {
        return resultsModel;
    }

    public void setResultsModel(ListDataModel resultsModel) {
        this.resultsModel = resultsModel;
    }

    public List<String> getStopNames() {
        return stopNames;
    }

    public void setStopNames(List<String> stopNames) {
        this.stopNames = stopNames;
    }

    public boolean isTimeForDeparture() {
        return timeForDeparture;
    }

    public void setTimeForDeparture(boolean isTimeForDeparture) {
        this.timeForDeparture = isTimeForDeparture;
    }

    public Integer[] getHoursFrom() {
        return hoursFrom;
    }

    public void setHoursFrom(Integer[] hoursFrom) {
        this.hoursFrom = hoursFrom;
    }

    public Integer[] getHoursTo() {
        return hoursTo;
    }

    public void setHoursTo(Integer[] hoursTo) {
        this.hoursTo = hoursTo;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public int getReturnFromHour() {
        return returnFromHour;
    }

    public void setReturnFromHour(int returnFromHour) {
        this.returnFromHour = returnFromHour;
    }

    public int getReturnToHour() {
        return returnToHour;
    }

    public void setReturnToHour(int returnToHour) {
        this.returnToHour = returnToHour;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturnTimeForDeparture() {
        return returnTimeForDeparture;
    }

    public void setReturnTimeForDeparture(boolean returnTimeForDeparture) {
        this.returnTimeForDeparture = returnTimeForDeparture;
    }

    public ListDataModel getReturnResultsModel() {
        return returnResultsModel;
    }

    public void setReturnResultsModel(ListDataModel returnResultsModel) {
        this.returnResultsModel = returnResultsModel;
    }

    public RunPriceHolder getSelectedRun() {
        return selectedRun;
    }

    public void setSelectedRun(RunPriceHolder selectedRun) {
        this.selectedRun = selectedRun;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public SimpleSelection getSelection() {
        return selection;
    }

    public void setSelection(SimpleSelection selection) {
        this.selection = selection;
    }

    public Long getSelectedRowId() {
        return selectedRowId;
    }

    public void setSelectedRowId(Long selectedRowId) {
        this.selectedRowId = selectedRowId;
    }
}
