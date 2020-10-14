package com.codewrox.keycloak.auth.validation;

public interface KCUserExtension extends KCExtension{
    boolean hasAttribute(String name);
    boolean hasGroup(String name);
    boolean hasRole(String name);
    String getAttributeValue(String attributeName);

}
