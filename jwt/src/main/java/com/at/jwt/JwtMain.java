package com.at.jwt;

import java.io.UnsupportedEncodingException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtMain {

    public String refreshJws(String jws) {
        log.debug("Entering refreshJws...");
        log.info("jws: '{}'", jws);
        Assert.notNull(jws, "jws should not be null.");
        
        Jws<Claims> jwsClaims = JwtUtils.verifyJws(jws);
        if(jwsClaims == null) {
            return null;
        }

        // exact uid
        String uid = jwsClaims.getBody().get("uid", String.class);
        
        String newJws = JwtUtils.genJws(uid);
        log.info("newJws: '{}'", newJws);

        // update user set jti = 'jti' where uid = 'uid';
        
        log.debug("Leaving refreshJws...");
        return newJws;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        log.info("Entering main.");
        
        String uid = "myuid";
        
        String jws = JwtUtils.genJws(uid);
        log.info("jws: '{}'", jws);
        Jws<Claims> jwsClaims =  JwtUtils.verifyJws(jws);
        log.info("jwsClaims: '{}'", jwsClaims);
        
        String newJws = new JwtMain().refreshJws(jws);
        log.info("newJws: '{}'", newJws);
        
        
        log.info("Exiting main.");
    }
}
