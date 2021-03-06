<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:p="http://primefaces.prime.com.tr/ui"
    xmlns:tc="http://tickets.com/tc">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tickets</title>
<link href="../css/main.css" type="text/css" rel="stylesheet" />
<style type="text/css">
.menuContent {
    vertical-align: middle;
}

.menuIcon {
    margin-right: 5px;
    vertical-align: middle;
}
.helpPanel {
    margin-left: 5px;
}

</style>

<ui:insert name="head" />
</head>
<f:view locale="#{localeController.locale}">
<body style="margin-left: 0px; margin-top: 0px; margin-right: 0px; text-align: left;">
<f:loadBundle var="msg" basename="com.tickets.constants.messages" />

<a4j:form id="toolbarForm">
    <a4j:poll action="#{keepAliveController.poll}" interval="500000"
        immediate="true" ajaxSingle="true" />

    <rich:toolBar itemSeparator="line" height="34">
        <rich:menuItem action="adminSearchScreen" id="searchMenuItem">
                <h:graphicImage value="/images/search.png" styleClass="menuIcon"
                    alt="#{msg.searchMenuItem}" />
                <h:outputText value="#{msg.runsMenuItem}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:menuItem action="routesList" rendered="#{loggedUserHolder.loggedUser.accessLevel.privileges >= 40}">
                <h:graphicImage value="/images/routes.png" styleClass="menuIcon"
                    alt="#{msg.routes}" />
                <h:outputText value="#{msg.routes}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:dropDownMenu style="padding-right: 27px; padding-left: 27px;"
            hideDelay="100" rendered="#{loggedUserHolder.loggedUser.accessLevel.privileges >= 40}">
            <f:facet name="label">
                <h:panelGroup>
                        <h:graphicImage value="/images/settings.png" styleClass="menuIcon"
                            alt="#{msg.settings}" />
                        <h:outputText value="#{msg.settings}" styleClass="menuContent" />
                </h:panelGroup>
            </f:facet>

            <rich:menuItem value="#{msg.users}" action="usersList"
                icon="/images/users.png" />

            <rich:menuItem value="#{msg.firmSettings}" ajaxSingle="true"
                oncomplete="#{rich:component('firmPanel')}.show()"
                icon="/images/firm.png" submitMode="ajax" reRender="firmPanel">
                <f:setPropertyActionListener
                    value="#{loggedUserHolder.loggedUser.firm}"
                    target="#{firmController.firm}" />
            </rich:menuItem>

            <rich:menuItem value="#{msg.discounts}" action="discountsList"
                icon="/images/discounts.png" />

            <!-- rich:menuItem value="#{msg.promotions}" action="promotionsList"
                icon="/images/promotions.png" /-->

            <rich:menuItem value="#{msg.offices}" action="officesList"
                icon="/images/offices.png" />

            <rich:menuItem value="#{msg.vehicles}" action="vehiclesList"
                icon="/images/vehicle.png" />

            <rich:menuItem value="#{msg.agents}" action="agentsList"
                icon="/images/agents.png" />

            <rich:menuItem value="#{msg.firms}" action="firmsList"
                icon="/images/agents.png"
                rendered="#{loggedUserHolder.loggedUser.accessLevel == 'ADMINISTRATOR'}" />

            <rich:menuItem value="#{msg.newsList}" action="newsList"
                icon="/images/discounts.png"
                rendered="#{loggedUserHolder.loggedUser.accessLevel == 'ADMINISTRATOR'}" />
        </rich:dropDownMenu>


        <rich:dropDownMenu style="padding-right: 27px; padding-left: 27px;"
            hideDelay="100" rendered="#{loggedUserHolder.loggedUser.accessLevel.privileges >= 40}">
            <f:facet name="label">
                <h:panelGroup>
                    <h:graphicImage value="/images/statistics.png"
                        styleClass="menuIcon" alt="#{msg.reports}" />
                    <h:outputText value="#{msg.reports}" styleClass="menuContent" />
                </h:panelGroup>
            </f:facet>

            <rich:menuItem value="#{msg.charts}" action="charts" />

            <rich:menuItem value="#{msg.tableReports}" action="tableReports" />

        </rich:dropDownMenu>

        <rich:menuItem action="#{loggedUserHolder.logout}"
            rendered="#{loggedUserHolder.loggedUser != null}">
                <h:graphicImage value="/images/logout.png" styleClass="menuIcon"
                    alt="#{msg.logout}" />
                <h:outputText value="#{msg.logout}" styleClass="menuContent" />
        </rich:menuItem>

        <rich:dropDownMenu
            rendered="#{loggedUserHolder.loggedUser.accessLevel == 'ADMINISTRATOR'}">
            <f:facet name="label">
                <a4j:region selfRendered="true" renderRegionOnly="true">
                    <h:selectOneMenu id="currentFirm"
                        value="#{loggedUserHolder.loggedUser.firm}"
                        converter="#{entityConverter}">
                        <f:selectItems value="#{firmController.firmsSelectItems}" />
                        <a4j:support event="onchange" action="#{routeController.init}" />
                    </h:selectOneMenu>
                </a4j:region>
            </f:facet>
        </rich:dropDownMenu>

        <rich:toolBarGroup location="right">
            <h:outputLink value="#{tc:getCurrentHelpPage(facesContext)}">
                    <h:graphicImage url="/images/small/help.png" alt="#{msg.help}"
                        title="#{msg.help}" style="border-style: none;" />
                </h:outputLink>
        </rich:toolBarGroup>
    </rich:toolBar>
</a4j:form>

<rich:modalPanel id="firmPanel" autosized="true" width="350"
    height="120" moveable="true" resizeable="false"
    domElementAttachment="parent">
    <ui:include src="/modalPanelCommons.jsp">
        <ui:param name="dialogId" value="firmPanel" />
    </ui:include>
    <f:facet name="header">
        <h:outputText value="#{msg.firm}" />
    </f:facet>
    <a4j:include viewId="firm.jsp" />
</rich:modalPanel>
<h:panelGroup style="text-align: left;" layout="block">

<h:outputText value="#{msg[param.errorKey]}"
    rendered="#{param.errorKey != null}" styleClass="error" />
<ui:insert name="body" />
</h:panelGroup>
</body>
</f:view>
</html>