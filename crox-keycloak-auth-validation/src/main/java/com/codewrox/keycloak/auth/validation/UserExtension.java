package com.codewrox.keycloak.auth.validation;


import org.jboss.logging.Logger;
import org.keycloak.models.*;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.models.utils.RoleUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserExtension implements KCUserExtension {
    private static final Logger LOG = Logger.getLogger(ClientAuthValidationAuthenticator.class);

    Map<String, Object> vars = new HashMap<>();
    private RealmModel realmModel;
    private UserModel userModel;
    private ClientModel clientModel;

    public UserExtension(RealmModel realmModel, UserModel userModel, ClientModel clientModel){
        this.realmModel = realmModel;
        this.userModel = userModel;
        this.clientModel = clientModel;
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
            return ""; //forgivable and returns empty for unavailable keys
        }
        return vars.get(k);
    }

    public String getAttributeValue(String attributeName){
        String attrValue = KeycloakModelUtils.resolveFirstAttribute(userModel, attributeName);
        LOG.debugf("attr:%s , value: %s ", attributeName, attrValue);
        return emptyIt(attrValue);
    }

    @Override
    public String getClientBaseUrl() {
        return emptyIt(this.clientModel.getBaseUrl());
    }

    @Override
    public String getClientRootUrl() {
        return emptyIt(this.clientModel.getRootUrl());
    }

    @Override
    public String getClientName() {
        return emptyIt(this.clientModel.getName());
    }

    @Override
    public String getClientId() {
        return emptyIt(this.clientModel.getClientId());
    }

    public boolean hasAttribute(String attributeName){
        String attrValue = KeycloakModelUtils.resolveFirstAttribute(userModel, attributeName);
        return  (attrValue != null);
    }

    private String emptyIt(String inp){
        return (inp==null)? "":  inp.trim();
    }

    @Override
    public boolean hasGroup(String groupPath) {
        GroupModel group = KeycloakModelUtils.findGroupByPath(this.realmModel, groupPath);
        return userModel.isMemberOf(group);
    }

    @Override
    public boolean hasRole(String roleName) {
        RoleModel role = KeycloakModelUtils.getRoleFromString(realmModel, roleName);
        return (RoleUtils.hasRole(userModel.getRoleMappings(), role) ||
                RoleUtils.hasRole(RoleUtils.getDeepUserRoleMappings(userModel), role));
    }



}