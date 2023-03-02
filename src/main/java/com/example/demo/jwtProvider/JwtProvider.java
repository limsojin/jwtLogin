package com.example.demo.jwtProvider;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.example.demo.repository.UserRepository;
import com.example.demo.user.User;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtProvider
{
    private final UserRepository userRepository;

    static Long EXPIRE_TIME = 60L * 60L * 1000L; // 만료 시간 1시간

    @Value ( "${jwt.secret}" )
    private String secretKey;

    /*
     * private Algorithm getSign ()
     * {
     * return Algorithm.HMAC512 ( this.secretKey );
     * }
     */

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init ()
    {
        this.secretKey = Base64.getEncoder ().encodeToString ( this.secretKey.getBytes () );
    }

    // Jwt 토큰 생성
    public String generateJwtToken ( Long id,
                                     String email,
                                     String username )
    {

        final Date tokenExpiration = new Date ( System.currentTimeMillis () + ( EXPIRE_TIME ) );

        final String jwtToken = Jwts.builder ()
                        .setSubject ( email ) // 토큰 이름
                        .withExpiresAt ( tokenExpiration )
                        .withClaim ( "id", id )
                        .withClaim ( "email", email )
                        .withClaim ( "username", username )
                        .sign ( this.getSign () );

        return jwtToken;
    }

    /**
     * 토큰 검증
     * - 토큰에서 가져온 email 정보와 DB의 유저 정보 일치하는지 확인
     * - 토큰 만료 시간이 지났는지 확인
     *
     * @param jwtToken
     * @return 유저 객체 반환
     */
    public User validToken ( String jwtToken )
    {
        try
        {

            final String email = Jwts.require ( this.getSign () )
                            .build ().verify ( jwtToken ).getClaim ( "email" ).asString ();

            // 비어있는 값이다.
            if ( email == null )
            {
                return null;
            }

            // EXPIRE_TIME이 지나지 않았는지 확인
            final Date expiresAt = Jwts.require ( this.getSign () ).acceptExpiresAt ( EXPIRE_TIME ).build ().verify ( jwtToken )
                            .getExpiresAt ();
            if ( !this.validExpiredTime ( expiresAt ) )
            {
                // 만료시간이 지났다.
                return null;
            }

            final User tokenUser = this.userRepository.findByEmail ( email );

            return tokenUser;

        } catch ( final Exception e )
        {
            e.printStackTrace ();
            return null;
        }

    }

    // 만료 시간 검증
    private boolean validExpiredTime ( Date expiresAt )
    {
        // LocalDateTime으로 만료시간 변경
        final LocalDateTime localTimeExpired = expiresAt.toInstant ().atZone ( ZoneId.of ( "Asia/Seoul" ) ).toLocalDateTime ();

        // 현재 시간이 만료시간의 이전이다
        return LocalDateTime.now ().isBefore ( localTimeExpired );

    }
}
