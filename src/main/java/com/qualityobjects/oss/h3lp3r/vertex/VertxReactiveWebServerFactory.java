package com.qualityobjects.oss.h3lp3r.vertex;

import java.util.LinkedList;
import java.util.List;

import com.qualityobjects.oss.h3lp3r.vertex.properties.HttpServerOptionsCustomizer;
import com.qualityobjects.oss.h3lp3r.vertex.properties.HttpServerProperties;

import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.http.server.reactive.HttpHandler;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

public class VertxReactiveWebServerFactory extends AbstractReactiveWebServerFactory {

    private final Vertx vertx;

    private final HttpServerProperties properties;

    private final List<HttpServerOptionsCustomizer> httpServerOptionsCustomizers = new LinkedList<>();

    public VertxReactiveWebServerFactory(Vertx vertx, HttpServerProperties properties) {
        this.vertx = vertx;
        this.properties = properties;
    }

    @Override
    public WebServer getWebServer(HttpHandler httpHandler) {
        HttpServerOptions httpServerOptions = customizeHttpServerOptions(properties.getHttpServerOptions());
        VertxHttpHandlerAdapter handler = new VertxHttpHandlerAdapter(httpHandler);

        return new VertxWebServer(vertx, httpServerOptions, handler);
    }

    public void registerHttpServerOptionsCustomizer(HttpServerOptionsCustomizer customizer) {
        httpServerOptionsCustomizers.add(customizer);
    }

    private HttpServerOptions customizeHttpServerOptions(HttpServerOptions httpServerOptions) {
        for (HttpServerOptionsCustomizer customizer : httpServerOptionsCustomizers) {
            httpServerOptions = customizer.apply(httpServerOptions);
        }
        return httpServerOptions;
    }
}
