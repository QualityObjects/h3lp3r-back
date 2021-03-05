package com.qualityobjects.oss.h3lp3r.filter;

import java.util.Map;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
public class OperationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        
        //System.out.println("HOLA ServerWebExchange");

        return chain.filter(exchange).contextWrite(Context.of(ServerHttpRequest.class, exchange.getRequest()));

        /*
                .subscriberContext(context -> {
                    var traceId = "";
                    if (headers.containsKey("X-B3-TRACEID")) {
                        traceId = headers.get("X-B3-TRACEID");
                        MDC.put("X-B3-TraceId", traceId);
                    } else if (!exchange.getRequest().getURI().getPath().contains("/actuator")) {
                        log.warn("TRACE_ID not present in header: {}", exchange.getRequest().getURI());
                    }

                    // simple hack to provide the context with the exchange, so the whole chain can get the same trace id
                    Context contextTmp = context.put("X-B3-TraceId", traceId);
                    exchange.getAttributes().put("X-B3-TraceId", traceId);

                    return contextTmp;
                });
                */

    }


    
}
