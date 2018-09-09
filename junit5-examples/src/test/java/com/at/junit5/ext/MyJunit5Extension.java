package com.at.junit5.ext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Random;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class MyJunit5Extension  implements ParameterResolver {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface RandInt {
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface RandDouble {
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface Msg {
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.isAnnotated(RandInt.class) || parameterContext.isAnnotated(RandDouble.class) || parameterContext.isAnnotated(Msg.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> type = parameterContext.getParameter().getType();
        Random random = new Random();
        if(String.class.equals(type)) {
            return "A message from '" + this.getClass().getSimpleName() + "'";
        }else if(int.class.equals(type) || Integer.class.equals(type)) {
            return random.nextInt(10);
        }else if(double.class.equals(type) || Double.class.equals(type)) {
            return random.nextDouble();
        }
        throw new ParameterResolutionException("Not Supported data type: '"+type+"'");
    }
}
