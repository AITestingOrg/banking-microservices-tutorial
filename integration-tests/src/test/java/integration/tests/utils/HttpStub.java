package integration.tests.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HttpStub {
    private Request request;
    private Response response;

    protected HttpStub(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String requestType;
        private String body;
        private String path;
        private String contentType;
        private int status;

        public Builder setRequestType(String requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public HttpStub build() {
            return new HttpStub(new Request(requestType, path), new Response(new Headers(contentType), body, status));
        }
    }

    public static class Request {
        private String method;
        private String url;

        public Request(String method, String url) {
            this.method = method;
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class Response {
        @JsonProperty("headers")
        private Headers headers;
        private String body;
        private int status;

        public Response(Headers headers, String body, int status) {
            this.headers = headers;
            this.body = body;
            this.status = status;
        }

        public Headers getHeaders() {
            return headers;
        }

        public String getBody() {
            return body;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class Headers {
        @JsonProperty("Content-Type")
        private String contentType;

        public Headers(String contentType) {
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }
    }
}
