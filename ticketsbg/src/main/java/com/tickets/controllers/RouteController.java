package com.tickets.controllers;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.ListDataModel;

import org.apache.myfaces.orchestra.conversation.Conversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.constants.Messages;
import com.tickets.model.Day;
import com.tickets.model.Route;
import com.tickets.services.RouteService;

@Controller("routeController")
@Scope("conversation.manual")
public class RouteController extends BaseController implements Serializable {

    private Route route;
    private ListDataModel routesModel;
    private List<Day> days;
    private Integer[] daysPickList;
    private Map<String, String> dayNames = new HashMap<String, String>();
    private Date hour;
    private int selectedHour;

    @Autowired
    private RouteService routeService;

    @Action
    public String save() {
        routeService.save(route, Arrays.asList(daysPickList));
        refreshList();
        return "routesList";
    }

    @Action
    public String edit(){
        route = (Route) routesModel.getRowData();
        daysPickList = routeService.getDaysList(route);
        return "routeScreen";
    }

    @Action
    public String delete() {
        routeService.delete((Route) routesModel.getRowData());
        refreshList();
        return "routesList";
    }

    @Action
    public String newRoute() {
        daysPickList = new Integer[0];
        route = new Route();
        return "routeScreen";
    }

    @Action
    public String addHour() {
        routeService.addHourToRoute(hour, route);
        return "routeScreen";
    }

    @Action
    public String removeHour() {
        routeService.removeHour(selectedHour, route);
        selectedHour = 0;
        return "routeScreen";
    }

    @PostConstruct
    public void init() {
        refreshList();
        route = new Route();
        days = routeService.list(Day.class);
        for (Day day : days) {
            day.setLabel(Messages.getString(day.getName()));
            dayNames.put(day.getName(), day.getLabel());
        }
    }

    private void refreshList() {
        routesModel = new ListDataModel(routeService.list());

        // End the current conversation in case the list of routes
        // is refreshed, but only if the bean has not just been constructed
        if (route != null)
            endConversation();
    }

    private void endConversation() {
        Conversation.getCurrentInstance().invalidate();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public ListDataModel getRoutesModel() {
        return routesModel;
    }

    public void setRoutesModel(ListDataModel routesModel) {
        this.routesModel = routesModel;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public Integer[] getDaysPickList() {
        return daysPickList;
    }

    public void setDaysPickList(Integer[] daysPickList) {
        this.daysPickList = daysPickList;
    }

    public Map<String, String> getDayNames() {
        return dayNames;
    }

    public void setDayNames(Map<String, String> dayNames) {
        this.dayNames = dayNames;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }

    public int getSelectedHour() {
        return selectedHour;
    }

    public void setSelectedHour(int selectedHour) {
        this.selectedHour = selectedHour;
    }
}
