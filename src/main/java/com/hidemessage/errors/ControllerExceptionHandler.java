package com.hidemessage.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        logger.error("Exception", ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> noSuchElementException(NoSuchElementException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage("Error. Request Element Not Found");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhoneNumberNotValidException.class)
    public ResponseEntity<ErrorMessage> phoneNumberNotValidException(PhoneNumberNotValidException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. Phone number %s is not valid.".formatted(ex.getPhoneNumber()));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PhoneNumberRegionNotSupportedException.class)
    public ResponseEntity<ErrorMessage> phoneNumberRegionNotSupportedException(
            PhoneNumberRegionNotSupportedException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. Region %s is not supported.".formatted(ex.getRegion()));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> usernameNotFoundException(UsernameNotFoundException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. User with phone number %s not found.".formatted(ex.getMessage()));
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SmsRateLimitException.class)
    public ResponseEntity<ErrorMessage> smsRateLimitException(SmsRateLimitException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. Too many requests. Please try again in %s seconds.".formatted(ex.getSecondsRemaining()));
        return new ResponseEntity<>(errorMessage, HttpStatus.TOO_EARLY);
    }

    @ExceptionHandler(SmsCodeExpiredException.class)
    public ResponseEntity<ErrorMessage> smsCodeExpiredException(SmsCodeExpiredException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. Sms code expired.");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SmsCodeMismatchException.class)
    public ResponseEntity<ErrorMessage> smsCodeMisMatchException(SmsCodeMismatchException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. Sms code mismatch.");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SmsServiceException.class)
    public ResponseEntity<ErrorMessage> smsRateException(SmsServiceException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. Sms service is not available.");
        return new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> illegalArgumentException(IllegalArgumentException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                "Error. Illegal argument.");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorMessage> unauthorizedException(UnauthorizedException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> badRequestException(BadRequestException ex) {
        logger.error("Exception", ex);
        ErrorMessage errorMessage = new ErrorMessage(
                ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }


}
