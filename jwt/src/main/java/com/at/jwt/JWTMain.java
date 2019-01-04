package com.at.jwt;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.message.FormattedMessage;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTMain {
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Decoder BASE64_DECODER = Base64.getDecoder();
    
    private static final SignatureAlgorithm SIGN_ALGO = SignatureAlgorithm.HS256;
    private static final String ISSUER = "AT";
    private static final long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;
    
    private static final byte[] DEFAULT_SEC_KEY_IN_BYTES = "mysecret".getBytes();
    
    private static final Key SIGN_KEY;
    static {
        byte[] secretKeyInBytes = DEFAULT_SEC_KEY_IN_BYTES;
        try {
            secretKeyInBytes = 
                    BASE64_DECODER.decode(
                        BASE64_ENCODER.encode(
                                "my long secret key configured in some other place".getBytes(CHARSET_UTF8)));
        } catch (UnsupportedEncodingException e) {
            log.error("somestring.getBytes({}) failed", CHARSET_UTF8, e);
        }
        SIGN_KEY = new SecretKeySpec(secretKeyInBytes,SIGN_ALGO.getJcaName());
    }
    

    public static void main(String[] args) throws UnsupportedEncodingException {
        log.info("Entering main.");
        StringBuilder outputsb = new StringBuilder();
        
        //// registered claims
        // iss  Issuer
        // sub  Subject
        // aud  Audience
        // exp  Expiration
        // nbf  Not Before
        // iat  Issued At
        // jti  JWT ID
        //
        //
        //// user defined claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "loginusername");
        claims.put("scope", "web.spring_boot.at.com");
        
        long nowInMs = System.currentTimeMillis();
        long expInMs = nowInMs + ONE_DAY_IN_MS;

        JwtBuilder jwtBuilder = 
                Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setIssuer(ISSUER)
                    .setSubject("genJWS")
                    .addClaims(claims)
//                    .claim("name", "Micah Silverman")
//                    .claim("scope", "admins")
                    .setIssuedAt(new Date(nowInMs))
                    .setExpiration(new Date(expInMs))
                    .signWith(SIGN_KEY, SIGN_ALGO)
                    ;
        
        { 
            // 1. compact form of jwt
            String compactJwt = jwtBuilder.compact();
            //      compactJwt: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBVCIsInN1YiI6ImdlbkpXUyIsInNjb3BlIjoid2ViLnNwcmluZ19ib290LmF0LmNvbSIsIm5hbWUiOiJsb2dpbnVzZXJuYW1lIiwiaWF0IjoxNTQ2MzQ1NTczLCJleHAiOjE1NDY0MzE5NzN9.fMZr3wmNz0pFFhmP8G6RhUpBY7iYrGhD9GQScSzgK-c'
            String compactJwtMsg = new FormattedMessage("compactJwt: '{}'", compactJwt).getFormattedMessage();
            outputsb.append(compactJwtMsg).append("\n");
            log.info(compactJwtMsg);

            // 2. checking the header and body without verification of
            //   split
            String[] jwtArr = compactJwt.split("\\.", -1); 
            assert jwtArr.length == 3 : "jwtArr.length != 3";
            
            //   deocde
            //      jwt decoded header: '{"typ":"JWT","alg":"HS256"}'
            String jwtDecodedHeaderMsg = new FormattedMessage("jwt decoded header: '{}'", new String(BASE64_DECODER.decode(jwtArr[0]), CHARSET_UTF8)).getFormattedMessage();
            outputsb.append(jwtDecodedHeaderMsg).append("\n");
            log.info(jwtDecodedHeaderMsg);
            
            //      jwt decoded body: '{"iss":"AT","sub":"genJWS","scope":"web.spring_boot.at.com","name":"loginusername","iat":1546345573,"exp":1546431973}'
            String jwtDecodedBodyMsg = new FormattedMessage("jwt decoded body: '{}'", new String(BASE64_DECODER.decode(jwtArr[1]), CHARSET_UTF8)).getFormattedMessage();
            outputsb.append(jwtDecodedBodyMsg).append("\n");
            log.info(jwtDecodedBodyMsg);
            
            
            // 3. JwtParser is used to verify the signature
            @SuppressWarnings("rawtypes")
            Jwt jwt = 
                    Jwts
                        .parser()
                        .setSigningKey(SIGN_KEY)
                        .parse(compactJwt)
                        ;
            //      jwt: 'header={typ=JWT, alg=HS256},body={iss=AT, sub=genJWS, scope=web.spring_boot.at.com, name=loginusername, iat=1546343921, exp=1546430321},signature=0vCXznv26EOFlpZ3oa2Az9-BoYvuMz4kndwQQ0ZSiq4'
            String jwtMsg = new FormattedMessage("jwt: '{}'", jwt).getFormattedMessage();
            outputsb.append(jwtMsg).append("\n");
            log.info(jwtMsg);
            
        }
        
        log.info("Exiting main.");
    }
}
