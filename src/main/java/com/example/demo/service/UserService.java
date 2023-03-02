package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.JoinRequestDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String join ( JoinRequestDto joinRequestDto )
    {
        // ...
        return "회원가입";
    }

    public String login ( LoginRequestDto loginRequestDto )
    {
        final String email = loginRequestDto.getEmail ();
        final String rawPassword = loginRequestDto.getPassword ();

        final User byEmail = this.userRepository.findByEmail ( email );

        // 비밀번호 일치 여부 확인
        if ( this.passwordEncoder.matches ( rawPassword, byEmail.getPasswd () ) )
        {
            return "로그인 성공";
        }

        return "로그인 실패";
    }
}
