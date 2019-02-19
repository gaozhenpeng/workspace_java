package com.at.lombok;

import java.io.IOException;
import java.util.Date;

import com.at.lombok.vo.CloseableResource;
import com.at.lombok.vo.Gender;
import com.at.lombok.vo.User;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LombokMain {
    public static void main(String[] args) throws IOException{
        log.info("LombokMain start: ");
        @Cleanup 
        CloseableResource closeableResource = new CloseableResource();
        
        log.info(() -> "User operation ");
        User user1 = new User("name1", Gender.MALE);
        User user2 = new User("name2", Gender.FEMALE);
        user2.setBirthday(new Date());
        
        log.info("add 1,2");
        closeableResource.add(1);
        closeableResource.add(2);

        log.info("noDeclarationOfThrownException");
        closeableResource.noDeclarationOfThrownException();

        log.info(() -> "LombokMain end; ");
        // auto close the closeableResource in a block of try-finally
    }
}
