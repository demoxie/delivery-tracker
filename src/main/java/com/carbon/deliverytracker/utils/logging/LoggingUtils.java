package com.carbon.deliverytracker.utils.logging;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoggingUtils {
    private LoggingUtils() {}
    public static String getLoggingMessage(String message, String path, String status, String method) {
        return String.format("Path: %s, Status: %s, Method: %s, Message: %s", path, status, method, message);
    }
    public static String logRequest(Object request, Object pathParams, Object queryParams, String path, String method) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return String.format("Request is being made to Path: %s, Method: %s, Request: %s, Path Params: %s, Query Params: %s", path, method, mapper.writeValueAsString(request), mapper.writeValueAsString(pathParams), mapper.writeValueAsString(queryParams));
        } catch (Exception e) {
            return String.format("Path: %s, Method: %s, Request: %s, Path Params: %s, Query Params: %s", path, method, request, pathParams, queryParams);
        }
    }

    public static String logResponse(Object response, String path, String method) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return String.format("Response is being sent to Path: %s, Method: %s, Response: %s", path, method, mapper.writeValueAsString(response));
        } catch (Exception e) {
            return String.format("Path: %s, Method: %s, Response: %s", path, method, response);
        }
    }
}
