package com.github.jmendex.kafka.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ElasticSearchJavaCodeDemo {

    public static RestHighLevelClient createClient(){

        String hostname = "kafka-8545535948.us-east-1.bonsaisearch.net"; // localhost or bonsai url
        String username = "69k51se8eh"; // needed only for bonsai
        String password = "52qkmo6wxy"; // needed only for bonsai

        // credentials provider help supply username and password
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostname, 443, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }


    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger(ElasticSearchJavaCodeDemo.class.getName());

        RestHighLevelClient client = createClient();

        String jsonString = "{ \"foo\": \"bar\" }";

        IndexRequest indexRequest = new IndexRequest(
                "twitter",
                "tweets"
        ).source(jsonString, XContentType.JSON);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        String id = indexResponse.getId();
        logger.info(id);

        client.close();

    }
}
