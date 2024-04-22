package com.hidemessage.routes;

import com.hidemessage.dao.UserDao;
import com.hidemessage.dtos.LoginRequest;
import com.hidemessage.dtos.LoginResponse;
import com.hidemessage.dtos.RegisterRequest;
import com.hidemessage.errors.BadRequestException;
import com.hidemessage.errors.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
public class UserRoute {

    private final UserDao userDao;

    @Autowired
    public UserRoute(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/me")
    @Operation(summary = "Get logged in user info.")
    public String getCurrentUserInfo() {
        return userDao.getLoggedUser().getEmail();
    }

    @PostMapping("/signin")
    @Operation(summary = "Login with email and password")
    public LoginResponse signin(@RequestBody LoginRequest loginRequest) throws UnauthorizedException {
        return userDao.login(loginRequest);
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up with email and password")
    public LoginResponse signup(@RequestBody RegisterRequest registerRequest) {
        return userDao.register(registerRequest);
    }


}
