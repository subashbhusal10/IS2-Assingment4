package com.hidemessage.dtos;


import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequest(

        String firstName,
        String lastName,
        String email,
        String password
) {


}
