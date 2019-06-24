package com.pupil.senabak.user.handler;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ErrorDetail {
    private String timestamp;
    private String status;
    private List<String> errors;

    ErrorDetail(HttpStatus status, List<String> errors) {
        this.timestamp = Instant.now().toString();
        this.status = status.value() + " (" + status.getReasonPhrase() + ")";
        this.errors = errors;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
