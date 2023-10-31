package com.example.ws.exception.reserveexception;

import com.example.ws.dto.Result;
import org.springframework.http.HttpStatus;

public class reserveException extends RuntimeException{
    private HttpStatus status;
    private Result errorResult;;


    protected reserveException(HttpStatus status, String message){
        this.status = status;
        this.errorResult = Result.createErrorResult(message);
    }
    public HttpStatus getStatus() {
        return this.status;
    }

    public Result getErrorResult() {
        return this.errorResult;
    }
}
