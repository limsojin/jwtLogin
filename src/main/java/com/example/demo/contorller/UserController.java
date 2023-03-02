package com.example.demo.contorller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.JoinRequestDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @PostMapping ( "/api/join" )
    public String join ( @RequestBody JoinRequestDto joinRequestDto )
    {

        return this.userService.join ( joinRequestDto );
    }

    @PostMapping ( "/api/login" )
    public String login ( @RequestBody LoginRequestDto loginRequestDto )
    {
        return this.userService.login ( loginRequestDto );
    }
}
