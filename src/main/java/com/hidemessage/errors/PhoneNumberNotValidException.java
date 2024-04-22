package com.hidemessage.errors;

import lombok.Getter;

@Getter
public class PhoneNumberNotValidException extends PhoneNumberException {

    private final String phoneNumber;

    public PhoneNumberNotValidException(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
