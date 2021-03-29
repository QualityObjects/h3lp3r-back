package com.qualityobjects.oss.h3lp3r.config;

import java.util.List;

import com.qualityobjects.oss.h3lp3r.repository.OperationLogRepository;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = OperationLogRepository.class)
public class ElasticSearchConfiguration  extends AbstractElasticsearchConfiguration {
    
    @Value("${spring.elasticsearch.rest.uris:localhost:9200}")
    private String[] urls;

    @Override
    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {
        System.out.println(List.of(urls));
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()  
            .connectedTo(urls)
            .build();

        return RestClients.create(clientConfiguration).rest();                         
    }

  
}
