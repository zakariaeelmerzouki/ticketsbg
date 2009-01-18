package com.tickets.client.constants;

import com.google.gwt.core.client.GWT;

public interface Messages extends com.google.gwt.i18n.client.Messages {
    public static Messages m = (Messages) GWT.create(Messages.class);

    String tickets();
    String sale();
    String check();

    String timeTable();
    String view();
    String routes();

    String username();
    String password();

    String login();
    String loginFailed();
    String email();
    String repeatPassword();
    String name();

    String emailInvalid();
    String forgottenPassword();
    String register();
    String successfulRegistration();
    String registrationFailed();
    String emailProblem();
    String incorrectLoginData();
    String userInactive();
}