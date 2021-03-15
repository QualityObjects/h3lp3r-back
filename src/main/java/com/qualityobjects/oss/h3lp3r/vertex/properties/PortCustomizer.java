package com.qualityobjects.oss.h3lp3r.vertex.properties;

import io.vertx.core.http.HttpServerOptions;
import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;

public class PortCustomizer implements HttpServerOptionsCustomizer {

    private final AbstractConfigurableWebServerFactory factory;

    public PortCustomizer(AbstractConfigurableWebServerFactory factory) {
        this.factory = factory;
    }

    @Override
    public HttpServerOptions apply(HttpServerOptions options) {
        if (factory.getPort() >= 0) {
            options.setPort(factory.getPort());
        }

        return options;
    }

}
