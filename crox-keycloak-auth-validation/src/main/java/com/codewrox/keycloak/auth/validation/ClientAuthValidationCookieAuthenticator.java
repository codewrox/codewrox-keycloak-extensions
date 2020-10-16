package com.codewrox.keycloak.auth.validation;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.protocol.LoginProtocol;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.sessions.AuthenticationSessionModel;


public class ClientAuthValidationCookieAuthenticator implements Authenticator {

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        AuthenticationManager.AuthResult authResult = AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                context.getRealm(), true);

        if (authResult == null) {
            context.attempted();
        } else {
            AuthenticationSessionModel clientSession = context.getAuthenticationSession();

            LoginProtocol protocol = context.getSession().getProvider(LoginProtocol.class, clientSession.getProtocol());

            // Cookie re-authentication is skipped if re-authentication is required
            if (protocol.requireReauthentication(authResult.getSession(), clientSession)) {
                context.attempted();
            } else {
                context.getSession().setAttribute(AuthenticationManager.SSO_AUTH, "true");

                context.setUser(authResult.getUser());
                context.attachUserSession(authResult.getSession());
                ClientAuthValidationAuthenticator clientAuthValidationAuthenticator = new ClientAuthValidationAuthenticator();
                clientAuthValidationAuthenticator.authenticate(context);

            }
        }

    }

    @Override
    public void action(AuthenticationFlowContext context) {

    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    @Override
    public void close() {

    }
}