package com.epam.wilma.extras.grpc.server;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrpcProperties {
    private final ServerProperties server;
    private final ErrorCodeMapping errorCodeBy;

    public GrpcProperties(ServerProperties server, ErrorCodeMapping errorCodeBy) {
        this.server = server;
        this.errorCodeBy = errorCodeBy;
    }

    public ServerProperties getServer() {
        return server;
    }

    public ErrorCodeMapping getErrorCodeBy() {
        return errorCodeBy;
    }

    public static class ServerProperties {
        private final Integer port;
        private final Integer maxHeaderListSize;
        private final Integer maxMessageSize;
        private final Integer maxInboundMetadataSize;
        private final Integer maxInboundMessageSize;

        public ServerProperties(Integer port, Integer maxHeaderListSize, Integer maxMessageSize, Integer maxInboundMetadataSize, Integer maxInboundMessageSize) {
            this.port = port;
            this.maxHeaderListSize = maxHeaderListSize;
            this.maxMessageSize = maxMessageSize;
            this.maxInboundMetadataSize = maxInboundMetadataSize;
            this.maxInboundMessageSize = maxInboundMessageSize;
        }

        public Integer getPort() {
            return port;
        }

        public Integer getMaxHeaderListSize() {
            return maxHeaderListSize;
        }

        public Integer getMaxMessageSize() {
            return maxMessageSize;
        }

        public Integer getMaxInboundMetadataSize() {
            return maxInboundMetadataSize;
        }

        public Integer getMaxInboundMessageSize() {
            return maxInboundMessageSize;
        }
    }

    public static class ErrorCodeMapping {
        private final Http http;

        public ErrorCodeMapping(Http http) {
            this.http = http;
        }

        public Http getHttp() {
            return http;
        }

        public static class Http {
            private final Map<Integer, String> statusCode = Stream.of(new Object[][]{
                    {400, "INVALID_ARGUMENT"},
                    {401, "UNAUTHENTICATED"},
                    {403, "PERMISSION_DENIED"},
                    {404, "NOT_FOUND"},
                    {409, "ALREADY_EXISTS"},
                    {429, "RESOURCE_EXHAUSTED"},
                    {499, "CANCELLED"},
                    {500, "INTERNAL"},
                    {501, "UNIMPLEMENTED"},
                    {503, "UNAVAILABLE"},
                    {504, "DEADLINE_EXCEEDED"}
            }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

            public Map<Integer, String> getStatusCode() {
                return statusCode;
            }
        }
    }
}
