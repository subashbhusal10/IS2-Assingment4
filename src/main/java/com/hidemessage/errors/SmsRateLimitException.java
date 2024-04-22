package com.hidemessage.errors;

import lombok.Getter;

@Getter
public class SmsRateLimitException extends SmsException {

    private final long secondsRemaining;

    public SmsRateLimitException(long secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
    }


}
