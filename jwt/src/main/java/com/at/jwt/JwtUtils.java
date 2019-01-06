package com.at.jwt;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Decoder BASE64_DECODER = Base64.getDecoder();
    
    /** SecureRandom is an alternative, slow */
    private static final Random RANDOM = new Random();
    
    private static final SignatureAlgorithm SIGN_ALGO = SignatureAlgorithm.HS256;
    private static final String ISSUER = "AT";
    private static final long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;
    
    private static final byte[] DEFAULT_SEC_KEY_IN_BYTES = "at least 256bits(32bytes) long secret".getBytes();
    
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
    

    public static String genJws(String uid) {
        log.debug("Entering genJws...");

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
        claims.put("uid", uid);
        claims.put("scope", "web.spring_boot.at.com");
        
        long nowInMs = System.currentTimeMillis();
        long expInMs = nowInMs + ONE_DAY_IN_MS;
        
        String jti = new UUID(RANDOM.nextLong(), RANDOM.nextLong()).toString();
        // save jti to db

        JwtBuilder jwtBuilder = 
                Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setIssuer(ISSUER)
                    .setSubject("jws")
                    .setId(jti)
                    .setIssuedAt(new Date(nowInMs))
                    .setExpiration(new Date(expInMs))
                    .addClaims(claims)
//                  .claim("name", "Micah Silverman")
//                  .claim("scope", "admins")
                    ;
        try {
            jwtBuilder.signWith(SIGN_KEY, SIGN_ALGO);
        }catch(InvalidKeyException e) {
            log.error("jws, siging key invalid, SIGN_KEY: '{}', SIGN_ALGO: '{}'. ", SIGN_KEY, SIGN_ALGO, e);
            return null;
        }
        String jws = jwtBuilder.compact();
        log.info("jws: '{}'", jws);
        log.debug("Leaving genJws...");
        return jws;
    }

    public static Jws<Claims> verifyJws(String jws) {
        log.debug("Entering verifyJws...");
        log.info("jws: '{}'", jws);
        Assert.notNull(jws, "jws should not be null.");
        

        if(log.isInfoEnabled()) {
            // checking the header and body without verification of split
            String[] jwtArr = jws.split("\\.", -1); 
            assert jwtArr.length == 3 : "jwtArr.length != 3";
            
            if(jwtArr.length >= 2) {
                try {
                    //   deocde
                    //      jwt decoded header: '{"typ":"JWT","alg":"HS256"}'
                    log.info("jwt decoded header: '{}'", new String(BASE64_DECODER.decode(jwtArr[0]), CHARSET_UTF8));
                    //      jwt decoded body: '{"iss":"AT","sub":"jws","uid":"myuid","scope":"web.spring_boot.at.com","iat":1546775316,"exp":1546861716}'
                    log.info("jwt decoded body: '{}'", new String(BASE64_DECODER.decode(jwtArr[1]), CHARSET_UTF8));
                } catch (UnsupportedEncodingException e) {
                    log.error("Decoding jws failed.", e);
                }
            }
        }

        // parse the jwt
        JwtParser jwtParser = Jwts.parser().setSigningKey(SIGN_KEY);
        Jws<Claims> jwsClaims = null;
        try{
            jwsClaims = jwtParser.parseClaimsJws(jws);
        }catch(UnsupportedJwtException e) {
            log.error("jwt (in this form) is not supported.", e);
            return null;
        }catch(ExpiredJwtException e) {
            log.error("jwt expired.", e);
            return null;
        }catch(MalformedJwtException e) {
            log.error("jwt form is incorrect.", e);
            return null;
        }catch(SignatureException e) {
            log.error("jwt signature is invalid.", e);
            return null;
        }catch(IllegalArgumentException e) {
            log.error("jwt argument is illegal.", e);
            return null;
        }

        //      jwsClaims: 'header={typ=JWT, alg=HS256},body={iss=AT, sub=jws, uid=myuid, scope=web.spring_boot.at.com, iat=1546775316, exp=1546861716},signature=EdaViVxqB5eK9tboDdtwSil90afWbEdhqalHvZrE0_0'
        log.info("jwsClaims: '{}'", jwsClaims);
        
        // check required fields
        
        
        log.debug("Leaving verifyJws...");
        return jwsClaims;
    }

}
