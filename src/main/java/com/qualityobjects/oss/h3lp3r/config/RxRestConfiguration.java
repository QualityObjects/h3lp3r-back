package com.qualityobjects.oss.h3lp3r.config;

import java.util.Set;

import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.vertex.VertxReactiveWebServerFactory;
import com.qualityobjects.oss.h3lp3r.vertex.VertxReactiveWebServerFactoryCustomizer;
import com.qualityobjects.oss.h3lp3r.vertex.VertxRequestUpgradeStrategy;
import com.qualityobjects.oss.h3lp3r.vertex.properties.HttpServerOptionsCustomizer;
import com.qualityobjects.oss.h3lp3r.vertex.properties.HttpServerProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.convert.ConverterBuilder;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import io.vertx.core.Vertx;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(ReactiveHttpInputMessage.class)
@ConditionalOnMissingBean(ReactiveWebServerFactory.class)
@EnableConfigurationProperties(HttpServerProperties.class)
public class RxRestConfiguration implements WebFluxConfigurer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(RxRestConfiguration.class);

	@Override
    public void addFormatters(FormatterRegistry registry) {
		
        registry.addConverter(ConverterBuilder.reading(String.class, Operation.class, (src) -> Operation.of(src)).getReadingConverter());
    }

    @Bean
    public Vertx getVertx() {
        return Vertx.vertx();
    }
	
    @Bean
    public VertxReactiveWebServerFactory vertxReactiveWebServerFactory(Vertx vertx, HttpServerProperties properties) {
        return new VertxReactiveWebServerFactory(vertx, properties);
    }

    @Bean
    public VertxReactiveWebServerFactoryCustomizer vertxWebServerFactoryCustomizer(
        Set<HttpServerOptionsCustomizer> userDefinedCustomizers) {
        return new VertxReactiveWebServerFactoryCustomizer(userDefinedCustomizers);
    }

    

}
