package com.at.java_native;

import java.util.Random;
import java.util.UUID;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UuidTest {
    

    @Test
    public void testUUID() {
        UUID uuid = null;
        // providing random implementation
        Random random = new Random();
        uuid = new UUID(random.nextLong(), random.nextLong());
        
        // use the default random implementation, default: SecureRandom
        uuid = UUID.randomUUID();
        String uuidHexStr = uuid.toString();
        String uuidHexStrReplaced = uuidHexStr.replaceAll("-", "");
        log.info("uuidHexStr: '{}'", uuidHexStr);
        log.info("uuidHexStrReplaced: '{}'", uuidHexStrReplaced);
        
        // The 'Hex' util of commons-codec can transform String to byte[], or vice versa
    }
}
