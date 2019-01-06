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

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtMain {
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
    

    public String genJws() {
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
        claims.put("name", "loginusername");
        claims.put("scope", "web.spring_boot.at.com");
        
        long nowInMs = System.currentTimeMillis();
        long expInMs = nowInMs + ONE_DAY_IN_MS;

        JwtBuilder jwtBuilder = 
                Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setIssuer(ISSUER)
                    .setSubject("jws")
                    .addClaims(claims)
//                    .claim("name", "Micah Silverman")
//                    .claim("scope", "admins")
                    .setIssuedAt(new Date(nowInMs))
                    .setExpiration(new Date(expInMs));
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

    public boolean verifyJws(String jws) {
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
                    String jwtDecodedHeaderMsg = new FormattedMessage("jwt decoded header: '{}'", new String(BASE64_DECODER.decode(jwtArr[0]), CHARSET_UTF8)).getFormattedMessage();
    
                    log.info("jwtDecodedHeaderMsg: '{}'", jwtDecodedHeaderMsg);
                    
                    //      jwt decoded body: '{"iss":"AT","sub":"jws","scope":"web.spring_boot.at.com","name":"loginusername","iat":1546345573,"exp":1546431973}'
                    String jwtDecodedBodyMsg = new FormattedMessage("jwt decoded body: '{}'", new String(BASE64_DECODER.decode(jwtArr[1]), CHARSET_UTF8)).getFormattedMessage();
                    log.info("jwtDecodedBodyMsg: '{}'", jwtDecodedBodyMsg);
                } catch (UnsupportedEncodingException e) {
                    log.error("Decoding jws failed.", e);
                }
            }
        }
        
        // 3. JwtParser is used to verify the signature
        JwtParser jwtParser = Jwts.parser().setSigningKey(SIGN_KEY);
        Jwt jwt = null;
        try{
            jwt = jwtParser.parse(jws);
        }catch(ExpiredJwtException e) {
            log.error("jwt expired.", e);
            return false;
        }catch(MalformedJwtException e) {
            log.error("jwt form is incorrect.", e);
            return false;
        }catch(SignatureException e) {
            log.error("jwt signature is invalid.", e);
            return false;
        }catch(IllegalArgumentException e) {
            log.error("jwt argument is illegal.", e);
            return false;
        }
        //      jwt: 'header={typ=JWT, alg=HS256},body={iss=AT, sub=genJWS, scope=web.spring_boot.at.com, name=loginusername, iat=1546343921, exp=1546430321},signature=0vCXznv26EOFlpZ3oa2Az9-BoYvuMz4kndwQQ0ZSiq4'
        log.info("jwt: '{}'", jwt);
        
        log.debug("Leaving verifyJws...");
        return true;
    }

    public String refreshJws( String jws) {
        log.debug("Entering refreshJws...");
        log.info("jws: '{}'", jws);
        Assert.notNull(jws, "jws should not be null.");
        
        if(!verifyJws(jws)) {
            return null;
        }
        
        String newJws = genJws();
        log.info("newJws: '{}'", newJws);
        
        
        log.debug("Leaving refreshJws...");
        return newJws;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        log.info("Entering main.");
        
        JwtMain jwtMain = new JwtMain();
        String jws = jwtMain.genJws();
        log.info("jws: '{}'", jws);
        boolean isJwsOk = jwtMain.verifyJws(jws);
        log.info("isJwsOk: '{}'", isJwsOk);
        String newJws = jwtMain.refreshJws(jws);
        log.info("newJws: '{}'", newJws);
        
        
        log.info("Exiting main.");
    }
}
