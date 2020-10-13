package com.codewrox.keycloak.auth.validation;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

public class AuthValidationTemplate {
    private static final Logger LOG = Logger.getLogger(AuthValidationTemplate.class);

    private final Configuration config = new Configuration(Configuration.VERSION_2_3_27);

    public AuthValidationTemplate() {
        config.setClassForTemplateLoading(AuthValidationTemplate.class, "");
        config.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_27));
    }

    public String render(String template, Object data) {
        try {
            Template templateEngine = new Template("validate", new StringReader(template),
                    config);
            StringWriter out = new StringWriter();
            templateEngine.process(new HashMap<String, Object>() {{
                put("user", data);
            }}, out);
            return out.toString();

        } catch (IOException | TemplateException e) {
            LOG.error(e);
        }
        return null;
    }



}
