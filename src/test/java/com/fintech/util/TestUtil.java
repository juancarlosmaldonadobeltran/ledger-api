package com.fintech.util;

import com.fintech.common.ApiResponse;
import com.fintech.common.JsonTransformer;
import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestUtil {

    private HttpClient httpClient;
    private JsonTransformer transformer;
    private String acceptType;
    private String uriBase;

    TestUtil() {
        this.httpClient = httpClientBuilder().build();
        this.transformer = new JsonTransformer();
        this.acceptType = "application/json";
        String protocol = "http";
        int port = 4567;
        this.uriBase = protocol + "://localhost:" + port;
    }

    public TestResponse get(String path) throws IOException {
        HttpGet httpGet = new HttpGet(uriBase + path);
        httpGet.setHeader("Accept", acceptType);
        return getResponse(httpGet);
    }

    public TestResponse post(String path, String body) throws IOException {
        HttpPost httpPost = new HttpPost(uriBase + path);
        httpPost.setHeader("Accept", acceptType);
        httpPost.setEntity(new StringEntity(body));
        return getResponse(httpPost);
    }

    private TestResponse getResponse(HttpUriRequest req) throws IOException {

        HttpResponse httpResponse = httpClient.execute(req);

        TestResponse.TestResponseBuilder testResponseBuilder = TestResponse.builder();
        testResponseBuilder.statusCode(httpResponse.getStatusLine().getStatusCode());
        Optional<HttpEntity> entity = Optional.ofNullable(httpResponse.getEntity());
        if (entity.isPresent()) {
            testResponseBuilder.body(transformer.fromJson(EntityUtils.toString(entity.get()), ApiResponse.class));
        }
        Map<String, String> headers = Arrays.stream(httpResponse.getAllHeaders())
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
        testResponseBuilder.headers(headers);
        return testResponseBuilder.build();
    }

    private HttpClientBuilder httpClientBuilder() {
        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
        return HttpClientBuilder.create().setConnectionManager(connManager);
    }

    @Data
    @Builder
    public static class TestResponse {

        private ApiResponse body;
        private int statusCode;
        private Map<String, String> headers;
    }

}