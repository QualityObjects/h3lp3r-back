package com.qualityobjects.oss.h3lp3r.vertex.properties;

import java.util.function.Function;

import io.vertx.core.http.HttpServerOptions;

@FunctionalInterface
public interface HttpServerOptionsCustomizer extends Function<HttpServerOptions, HttpServerOptions> {

}
