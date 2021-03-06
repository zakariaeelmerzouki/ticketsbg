<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:j4j="http://javascript4jsf.dev.java.net/"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:tc="http://tickets.com/tc"
    xmlns:t="http://myfaces.apache.org/tomahawk">
    <f:view>
        <h:messages />
        <h:form id="discountForm">
            <a4j:outputPanel ajaxRendered="true">
                <h:panelGrid columns="2">
                    <h:outputLabel value="#{msg.discountName}: " for="discountName" />
                    <rich:comboBox value="#{routeController.discount.name}"
                        id="discountName" listStyle="text-align: left;"
                        suggestionValues="#{routeController.genericDiscountNames}"
                        width="150px">
                        <a4j:support event="onselect"
                            action="#{routeController.genericDiscountSelected}"
                            reRender="description" />
                    </rich:comboBox>

                    <h:outputLabel value="#{msg.discountType}: " for="discountType" />
                    <h:selectOneMenu value="#{routeController.discount.discountType}"
                        id="discountType" style="width: 150px;">
                        <f:selectItems value="#{routeController.discountTypeSelectItems}" />
                    </h:selectOneMenu>

                    <a4j:outputPanel>
                        <h:outputLabel value="#{msg.discountValue}: " for="discountValue" />
                        <rich:toolTip followMouse="true" value="#{msg.discountValueHint}"/>
                    </a4j:outputPanel>
                    <h:inputText value="#{routeController.discount.value}" id="discountValue" style="width: 150px;">
                        <f:convertNumber maxFractionDigits="2" minFractionDigits="2" />
                    </h:inputText>

                    <a4j:outputPanel>
                        <h:outputLabel value="#{msg.discountTwoWayValue}: " for="discountTwoWayValue" style="width: 150px;" />
                        <rich:toolTip followMouse="true" value="#{msg.discountValueHint}"/>
                    </a4j:outputPanel>
                    <h:inputText value="#{routeController.discount.twoWayValue}" id="discountTwoWayValue">
                        <f:convertNumber maxFractionDigits="2" minFractionDigits="2" />
                    </h:inputText>

                    <h:outputLabel value="#{msg.startStop}" for="discountStartStop" />
                    <h:selectOneMenu value="#{routeController.discount.startStop}"
                        id="discountStartStop">
                        <f:selectItems value="#{routeController.stopSelectItems}" />
                    </h:selectOneMenu>

                    <h:outputLabel value="#{msg.endStop}" for="discountEndStop" />
                    <h:selectOneMenu value="#{routeController.discount.endStop}"
                        id="discountEndStop">
                        <f:selectItems value="#{routeController.stopSelectItems}" />
                    </h:selectOneMenu>

                    <h:outputLabel value="#{msg.currentDayOnly}" for="currentDayOnly" />
                    <h:selectBooleanCheckbox
                        value="#{routeController.discount.currentDayOnly}"
                        id="currentDayOnly" />

                    <h:outputLabel value="#{msg.description}: " for="description" />
                    <h:inputTextarea value="#{routeController.discount.description}"
                        id="description" rows="5" cols="25" />


                    <a4j:commandButton action="#{routeController.saveDiscount}"
                        value="#{msg.save}" reRender="discountsPanel"
                        oncomplete="#{rich:component('discountPanel')}.hide();">
                    </a4j:commandButton>
                    <h:outputText></h:outputText>
                </h:panelGrid>
            </a4j:outputPanel>
        </h:form>
    </f:view>
</ui:composition>