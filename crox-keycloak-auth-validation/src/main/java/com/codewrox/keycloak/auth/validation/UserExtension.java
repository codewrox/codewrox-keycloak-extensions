package com.codewrox.keycloak.auth.validation;


import org.jboss.logging.Logger;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class UserExtension implements KCExtension {
    private static final Logger LOG = Logger.getLogger(ClientValidationAuthenticator.class);

    Map<String, Object> vars = new HashMap<>();
    private RealmModel realmModel;
    private UserModel userModel;

    public UserExtension(RealmModel realmModel, UserModel userModel){

        this.realmModel = realmModel;
        this.userModel = userModel;
    }

    public void set(String k, Object o){
        vars.put(k, o);
        LOG.debug("key:"+k+" value:"+o.toString());
    }

    @Override
    public boolean getAsBoolean(String k) {
        return Boolean.parseBoolean( get(k).toString() );
    }

    @Override
    public String getAsString(String k) {
        return get(k).toString();
    }

    public Object get(String k){
        if (!vars.containsKey(k)) {
            LOG.debug("value missing for key: " + k);
            return "";
        }
        return vars.get(k);
    }

    public int add(int a, int b) {
        return a + b;
    }

    public int mul(int a, int b) {
        return a * b;
    }


}