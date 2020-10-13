package com.codewrox.keycloak.auth.validation;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jboss.logging.Logger;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

public class ValidationTemplate {
    private static final Logger LOG = Logger.getLogger(ValidationTemplate.class);

    private final Configuration config = new Configuration(Configuration.VERSION_2_3_27);
    private final RealmModel realmModel;
    private final UserModel userModel;

    public ValidationTemplate(RealmModel realmModel, UserModel userModel) {
        this.realmModel = realmModel;
        this.userModel = userModel;
        config.setClassForTemplateLoading(ValidationTemplate.class, "");
        config.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_27));
    }

    public boolean evaluate(String template) {
        try {
            Template templateEngine = new Template("validate", new StringReader(template),
                    config);
            StringWriter out = new StringWriter();
            templateEngine.process(new HashMap<String, Object>() {{
                put("user", new UserExtension());
            }}, out);
            LOG.error("template out:"+out.toString());
            return Boolean.parseBoolean(out.toString());

        } catch (IOException | TemplateException e) {
            LOG.error(e);
        }
        return false;
    }


    public static class UserExtension {

        public int add(int a, int b) {
            return a + b;
        }

        public int mul(int a, int b) {
            return a * b;
        }


    }

}
