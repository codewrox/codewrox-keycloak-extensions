package com.codewrox.keycloak.auth.validation;

import java.util.Set;

public interface KCUserExtension extends KCExtension{
    boolean hasAttribute(String name);
    boolean hasGroup(String name);
    boolean hasRole(String name);
    String getAttributeValue(String attributeName);
    String getClientBaseUrl();
    String getClientRootUrl();
    String getClientName();
    String getClientId();
}
