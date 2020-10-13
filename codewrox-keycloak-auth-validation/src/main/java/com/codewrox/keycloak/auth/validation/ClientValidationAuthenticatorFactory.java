package com.codewrox.keycloak.auth.validation;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

public class ClientValidationAuthenticatorFactory implements AuthenticatorFactory {

    public static final String PROP_VALIDATION_TEMPLATE = "validation_template";
    public static final ClientValidationAuthenticator clientValidator = new ClientValidationAuthenticator();
    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };
    private static final String PROVIDER_ID = "codewrox-auth-validation";

    @Override
    public String getDisplayType() {
        return "Client Auth Validation";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Validates the template and finally it return true or false";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {

        ProviderConfigProperty validation_template = new ProviderConfigProperty() {
            {
                setLabel("Validation template");
                setName(PROP_VALIDATION_TEMPLATE);
                setHelpText("Validates the template and finally it return true or false");
                setType(ProviderConfigProperty.TEXT_TYPE);
            }
        };

        return Arrays.asList(validation_template);
    }


    @Override
    public Authenticator create(KeycloakSession session) {
        return clientValidator;
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
