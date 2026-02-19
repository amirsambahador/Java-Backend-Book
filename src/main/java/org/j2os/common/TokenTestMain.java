package org.j2os.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class TokenTestMain {
    static void main() {
        var token = JWT.create()
                .withSubject("bahador")
                .withClaim("email", "amirsam.bahador@gmail.com")
                .withClaim("bd", "1986")
                .withClaim("name", "amirsam")
                .withClaim("family", "bahador")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer("j2os.org")
                .withExpiresAt(new Date(System.currentTimeMillis() - 1000))
                .sign(Algorithm.HMAC512("aasd$123daasd@qfxbg"));
        System.out.println(token);

        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512("aasd$123daasd@qfxbg"))
                    .withIssuer("j2os.org")
                    .build()
                    .verify(token);
            System.out.println(decoded.getSubject());
            System.out.println(decoded.getClaim("email").asString());
            System.out.println(decoded.getClaim("bd").asString());
            System.out.println(decoded.getClaim("name").asString());
            System.out.println(decoded.getClaim("family").asString());
        } catch (TokenExpiredException e) {
            System.out.println("توکن فاسد شده است.");
        } catch (SignatureVerificationException e) {
            System.out.println("توکن جعل شده است");
        } catch (Exception e) {
            System.out.println("اتفاق فیر عادی پیش بینی نشده!");
        }
    }
}
























