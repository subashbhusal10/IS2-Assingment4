package com.hidemessage.errors;

import lombok.Getter;

@Getter
public class PhoneNumberRegionNotSupportedException extends PhoneNumberException {


    private final String region;

    public PhoneNumberRegionNotSupportedException(String region) {
        this.region = region;
    }

}
