package com.qualityobjects.oss.h3lp3r.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.qualityobjects.oss.h3lp3r.exception.QORuntimeException;
import com.qualityobjects.oss.h3lp3r.repository.OperationLogRepository;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import lombok.Data;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = OperationLogRepository.class)
public class ElasticSearchConfiguration  extends AbstractElasticsearchConfiguration {
    
    @Value("${spring.elasticsearch.rest.uris:localhost:9200}")
    private String[] urls;

    @Override
    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {
            
        HttpHost[] hosts = (HttpHost[])Arrays.stream(urls).map(url -> {
            var node = ElasticSearchNode.of(url);
            return new HttpHost(node.host, node.port);
        }).toArray();
        return  new RestHighLevelClient(RestClient.builder(hosts));
    }

    @Data
    private static class ElasticSearchNode {
        private static final int DEFAULT_PORT = 9200;

        private final InetAddress host;
        private final int port;

        public static ElasticSearchNode of(String url) {
            String[] parts = url.split(":");
            InetAddress host;
            try {
                host = InetAddress.getByName(parts[0]);
            } catch (UnknownHostException e) {
                throw new QORuntimeException("Error configuring ElasticSearch client", e);
            }
            int port = (parts.length > 1) ? Integer.parseInt(parts[1]) : DEFAULT_PORT;
            return new ElasticSearchNode(host, port);
        }
    }

  
}
