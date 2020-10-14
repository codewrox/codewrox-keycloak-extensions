package com.codewrox.keycloak.auth.validation;


import org.jboss.logging.Logger;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.models.utils.RoleUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserExtension implements KCUserExtension {
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

    public String getAttributeValue(String attributeName){
        String attrValue = KeycloakModelUtils.resolveFirstAttribute(userModel, attributeName);
        LOG.debugf("attr:%s , value: %s ", attributeName, attrValue);
        return attrValue;
    }

    public boolean hasAttribute(String attributeName){
        return getAttributeValue(attributeName) != null;
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