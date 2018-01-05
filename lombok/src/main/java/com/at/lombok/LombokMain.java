package com.at.lombok;

import java.io.IOException;
import java.util.Date;

import com.at.lombok.vo.CloseableResource;
import com.at.lombok.vo.Gender;
import com.at.lombok.vo.User;

import lombok.Cleanup;

public class LombokMain {
    public static void main(String[] args) throws IOException{
        @Cleanup 
        CloseableResource closeableResource = new CloseableResource();
        User user1 = new User("name1", Gender.MALE);
        User user2 = new User("name2", Gender.FEMALE);
        user2.setBirthday(new Date());
        

        closeableResource.add(1);
        closeableResource.add(2);
        
        closeableResource.noDeclarationOfThrownException();
        
        // auto close the closeableResource in a block of try-finally
    }
}
