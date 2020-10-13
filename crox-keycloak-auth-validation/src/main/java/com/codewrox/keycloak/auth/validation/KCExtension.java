package com.codewrox.keycloak.auth.validation;

public interface KCExtension {
    Object get(String k);
    void set(String k, Object o);
    boolean getAsBoolean(String k);
    String getAsString(String k);
}
