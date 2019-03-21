package com.ultimatesoftware.banking.transactions.mocks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.ultimatesoftware.banking.transactions.utils.StringTools.convertStreamToString;

public class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
    private final String path;
    private final String host;
    private final int port;
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected HttpClient(String path, String host, int port) {
        this.path = path;
        this.host = host;
        this.port = port;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public <T> T get(String id, Class<T> type) throws IOException {
        URL obj = new URL(String.format("http://%s:%d%s/%s", host, port, path, id));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(3000);
        int statusCode = con.getResponseCode();
        if (statusCode == 200) {
            LOG.info(String.format("GET request for type %s with status code: %d", type, statusCode));
            return objectMapper.readValue(con.getInputStream(), type);
        } else {
            LOG.info(String.format("GET request for type %s with status code: %d", type, statusCode));
            return objectMapper.readValue(con.getErrorStream(), type);
        }
    }

    public <T> List<T> get(Class<T> type) throws IOException {
        URL obj = new URL(String.format("http://%s:%d%s", host, port, path));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(3000);
        int statusCode = con.getResponseCode();
        if (statusCode == 200 || statusCode == 201) {
            LOG.info(String.format("GET request for type %s with status code: %d", type, statusCode));
            return objectMapper.readValue(con.getInputStream(), new TypeReference<List<T>>(){});
        } else {
            LOG.info(String.format("GET request for type %s with status code: %d", type, statusCode));
            return objectMapper.readValue(con.getErrorStream(), new TypeReference<List<T>>(){});
        }

    }

    public <T> ResponseDto post(T entity, String path) throws IOException {
        URL obj = new URL(String.format("http://%s:%d%s", host, port, this.path + path));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(3000);
        String body = objectMapper.writeValueAsString(entity);
        if (entity instanceof String) {
            body = (String) entity;
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.getOutputStream().write(body.getBytes("UTF8"));
        int statusCode = con.getResponseCode();
        if (statusCode == 200 || statusCode == 201) {
            LOG.info(String.format("GET request with status code: %d", statusCode));
            return new ResponseDto(convertStreamToString(con.getInputStream()), statusCode);
        } else {
            LOG.info(String.format("GET request with status code: %d", statusCode));
            return new ResponseDto(convertStreamToString(con.getErrorStream()), statusCode);
        }
    }

    public void delete() throws IOException {
        URL obj = new URL(String.format("http://%s:%d%s", host, port, this.path));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("DELETE");
        int statusCode = con.getResponseCode();
        if (statusCode == 200 || statusCode == 201) {
            LOG.info(String.format("DELETE request with status code: %d", statusCode));
        } else {
            LOG.info(String.format("DELETE request with status code: %d", statusCode));
        }
    }

    public static class Builder<T> {
        private String path;
        private String host;
        private int port;

        public Builder<T> setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder<T> setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder<T> setPort(int port) {
            this.port = port;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(path, host, port);
        }
    }
}
