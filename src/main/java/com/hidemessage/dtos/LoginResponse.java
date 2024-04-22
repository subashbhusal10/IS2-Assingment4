package com.hidemessage.dtos;


public record LoginResponse(

        String accessToken,
        String refreshToken

) {

}
