package com.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.model.entity.Admin;
import com.model.enums.RoleName;
import com.security.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtProviderTest {
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void generateTokenTest() {

        // Arrange
        Admin user = new Admin("mjonli@mail.com",null, RoleName.ROLE_CLIENT.toString());

        user.setId(5l);

        // Act
        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = JWT.decode(token);

        // Assert
        assertNotNull(token);
    }

    @Test
    public void verifyValidTokenTest() {

        // Arrange
        Admin user = new Admin("mjonli@mail.com",null, RoleName.ROLE_CLIENT.toString());

        user.setId(5l);

        // Act
        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);

        // Assert
        assertEquals(user.getId().toString(), decodedToken.getSubject());
        assertEquals(user.getRole(), decodedToken.getClaim("role").asString());
    }

    //TODO fix this test
//    @Test
//    public void verifyInvalidTokenTest() {
//
//        // Arrange
//        Admin user = new Admin("mjonli@mail.com",null, RoleName.ROLE_CLIENT.toString());
//
//        user.setId(5l);
//
//        // Act
//        final String token = jwtProvider.generate(user);
//        final StringBuilder sb = new StringBuilder(token);
//
//        sb.deleteCharAt(token.length() / 2); // Should not be able to modify token
//
//        System.out.println(sb.toString());
//
//        //Assert
//        assertThrows(JWTVerificationException.class, () -> jwtProvider.verify(sb.toString().trim()));
//    }
}
