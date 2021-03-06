<?xml version="1.0" encoding="UTF-8" ?>
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:fmt="http://java.sun.com/jstl/fmt"
    xmlns:t="http://myfaces.apache.org/tomahawk"
    xmlns:p="http://primefaces.prime.com.tr/ui"
    template="publicTemplate.jsp">

    <ui:define name="body">
        <f:view>
            <rich:panel header="#{msg.successfulRegistrationTitle}"
                headerClass="rich-panel-header-main">
                <div align="center" style="width: 100%;">
                    <h:outputText
                       value="#{msg.successfulRegistration}" style="color: green;" /></div>
            </rich:panel>
        </f:view>
    </ui:define>
</ui:composition>