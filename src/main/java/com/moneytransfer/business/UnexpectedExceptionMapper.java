package com.moneytransfer.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.dao.Exceptions.ApplicationException;
import com.moneytransfer.dao.Exceptions.InvalidAccountException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/***
 * This class creates a json response for the ApplicationException thrown by the APIs.
 */
@Provider
public class UnexpectedExceptionMapper implements ExceptionMapper<ApplicationException> {

    @Override
    public Response toResponse(final ApplicationException exception) {
        ErrorInfo errorInfo = new ErrorInfo(exception.getMessage());
        Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND)
                .entity(errorInfo)
                .type(MediaType.APPLICATION_JSON);
        return builder.build();
    }
}

class ErrorInfo {
    String errorMessage;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    ErrorInfo(String message) {
        this.errorMessage = message;
    }
}
