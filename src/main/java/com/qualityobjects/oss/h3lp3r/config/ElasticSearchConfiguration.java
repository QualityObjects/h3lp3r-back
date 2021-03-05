package com.qualityobjects.oss.h3lp3r.config;

import java.util.List;

import com.qualityobjects.oss.h3lp3r.repository.OperationLogRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = OperationLogRepository.class)
public class ElasticSearchConfiguration  extends AbstractReactiveElasticsearchConfiguration {
    
    @Value("${spring.elasticsearch.rest.uris:localhost:9200}")
    private String[] urls;

    @Override
    @Bean
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        System.out.println(List.of(urls));
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()  
            .connectedTo(urls)
            .build();

        return ReactiveRestClients.create(clientConfiguration);
    }

  
}
