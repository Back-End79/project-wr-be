package com.tujuhsembilan.wrcore.configuration;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import com.toedter.spring.hateoas.jsonapi.JsonApiError;
import lib.minio.exception.MinioServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResponseEntityExceptionConfig extends ResponseEntityExceptionHandler {

  private static String getStatusString(HttpStatus status) {
    return status.series().name() + ":" + status.value() + ":" + status.name();
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    super.handleExceptionInternal(ex, body, headers, status, request);
    return ResponseEntity
        .status(status)
        .headers(headers)
        .body(body != null ? body
            : JsonApiError.create()
                .withStatus(getStatusString(status))
                .withTitle(status.getReasonPhrase())
                .withDetail(ex.getLocalizedMessage()));
  }

  @ExceptionHandler({
      Exception.class
  })
  protected ResponseEntity<Object> handleAnyOtherException(Exception ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    Object body = null;

    if (ex instanceof EntityNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex instanceof MinioServiceException) {
      body = JsonApiError.create()
          .withStatus(getStatusString(status))
          .withTitle(ex.getLocalizedMessage())
          .withDetail(ex.getCause().getLocalizedMessage());
    } else if (ex instanceof ValidationException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof EntityExistsException) {
      status = HttpStatus.BAD_REQUEST;      
    } else {
      // do nothing?
    }



    return this.handleExceptionInternal(ex, body, headers, status, request);
  }

}
