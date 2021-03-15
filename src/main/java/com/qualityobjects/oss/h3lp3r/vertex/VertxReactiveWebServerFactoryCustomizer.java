package com.qualityobjects.oss.h3lp3r.vertex;

import java.util.Set;

import com.qualityobjects.oss.h3lp3r.vertex.properties.AddressCustomizer;
import com.qualityobjects.oss.h3lp3r.vertex.properties.CompressionCustomizer;
import com.qualityobjects.oss.h3lp3r.vertex.properties.HttpServerOptionsCustomizer;
import com.qualityobjects.oss.h3lp3r.vertex.properties.PortCustomizer;
import com.qualityobjects.oss.h3lp3r.vertex.properties.SslCustomizer;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;

public class VertxReactiveWebServerFactoryCustomizer
    implements WebServerFactoryCustomizer<VertxReactiveWebServerFactory>, Ordered {

    private final Set<HttpServerOptionsCustomizer> userDefinedCustomizers;

    public VertxReactiveWebServerFactoryCustomizer(Set<HttpServerOptionsCustomizer> userDefinedCustomizers) {
        this.userDefinedCustomizers = userDefinedCustomizers;
    }

    @Override
    public void customize(VertxReactiveWebServerFactory factory) {
        factory.registerHttpServerOptionsCustomizer(new PortCustomizer(factory));
        factory.registerHttpServerOptionsCustomizer(new AddressCustomizer(factory));
        factory.registerHttpServerOptionsCustomizer(new SslCustomizer(factory));
        factory.registerHttpServerOptionsCustomizer(new CompressionCustomizer(factory));

        if (userDefinedCustomizers != null) {
            userDefinedCustomizers.forEach(factory::registerHttpServerOptionsCustomizer);
        }
    }

    @Override
    public int getOrder() {
        return 1; // Run after ReactiveWebServerFactoryCustomizer
    }
}
