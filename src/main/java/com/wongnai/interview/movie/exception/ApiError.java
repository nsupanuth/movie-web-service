package com.wongnai.interview.movie.exception;

import java.util.Date;

public class ApiError {

    private Date timestamp;
    private String entityName;
    private int status;
    private String error;
    private String title;
    private String message;

    public ApiError() {}

    public ApiError(Date timestamp, String entityName, int status, String error, String title, String message) {
        this.timestamp = timestamp;
        this.entityName = entityName;
        this.status = status;
        this.error = error;
        this.title = title;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
