package com.codewrox.keycloak.auth.validation;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;


public class ClientValidationAuthenticator implements Authenticator {

    private static final Logger LOG = Logger.getLogger(ClientValidationAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        AuthenticatorConfigModel configModel = context.getAuthenticatorConfig();
        String template = configModel.getConfig().get(ClientValidationAuthenticatorFactory.PROP_VALIDATION_TEMPLATE);
       // LOG.errorf("template=%s", template);
        ValidationTemplate validationTemplate = new ValidationTemplate(context.getRealm(), context.getUser());
        LOG.errorf("isValid=%s", validationTemplate.evaluate(template));


        context.success();
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {

    }


    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }


}
