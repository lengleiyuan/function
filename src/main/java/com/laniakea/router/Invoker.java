package com.laniakea.router;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author wb-lgc489196
 * @version Invoker.java, v 0.1 2019年08月16日 16:20 wb-lgc489196 Exp
 */
public class Invoker implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return  method.invoke(proxy, args);
    }
}
