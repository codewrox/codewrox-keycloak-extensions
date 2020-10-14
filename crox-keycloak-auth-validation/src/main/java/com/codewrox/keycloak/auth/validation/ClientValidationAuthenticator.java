package com.codewrox.keycloak.auth.validation;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


public class ClientValidationAuthenticator implements Authenticator {

    private static final Logger LOG = Logger.getLogger(ClientValidationAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        AuthenticatorConfigModel configModel = context.getAuthenticatorConfig();
        String template = configModel.getConfig().get(ClientValidationAuthenticatorFactory.PROP_VALIDATION_TEMPLATE);
        String redirect_url = configModel.getConfig().get(ClientValidationAuthenticatorFactory.PROP_REDIRECT_URL);
        KCUserExtension userExtension = new UserExtension(context.getRealm(), context.getUser());
        AuthValidationTemplate authValidationTemplate = new AuthValidationTemplate();
        authValidationTemplate.render(template, userExtension);
        if (!userExtension.getAsBoolean("allow_access")) {
            LOG.debugf("Permission denied because of missing one or more pre auth conditions. Please contact administrator.");
            context.cancelLogin();
            if ((redirect_url != null) && (!redirect_url.isEmpty())) {
                try {
                    Response response = Response.seeOther(new URI(authValidationTemplate.render(redirect_url, userExtension))).build();
                    context.forceChallenge(response);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            return;
        }

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
