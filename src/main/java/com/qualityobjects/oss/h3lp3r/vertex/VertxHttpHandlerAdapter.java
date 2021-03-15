package com.qualityobjects.oss.h3lp3r.vertex;

import com.qualityobjects.oss.h3lp3r.vertex.utils.BufferConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class VertxHttpHandlerAdapter implements Handler<RoutingContext> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HttpHandler httpHandler;

    private final BufferConverter bufferConverter;

    public VertxHttpHandlerAdapter(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
        this.bufferConverter = new BufferConverter();
    }

    @Override
    public void handle(RoutingContext context) {
        logger.debug("Adapting Vert.x server request to WebFlux request");

        VertxServerHttpRequest webFluxRequest = new VertxServerHttpRequest(context, bufferConverter);
        VertxServerHttpResponse webFluxResponse = new VertxServerHttpResponse(context, bufferConverter);

        httpHandler.handle(webFluxRequest, webFluxResponse)
            .doOnSuccess(v -> {
                    logger.debug("Completed server request handling");
                    if (!context.response().ended()) {
                        context.response()
                            .end();
                    }
                }
            )
            .doOnError(throwable -> {
                    logger.debug("Completed server request handling with an error '{}'", throwable.toString());
                    context.response()
                        .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .end();
                }
            )
            .subscribe();
    }

}
