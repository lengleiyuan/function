package com.laniakea.router;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.laniakea.router.InvokerManager.ERROR_CODE;

/**
 * @author wb-lgc489196
 * @version Invoker.java, v 0.1 2019年08月16日 16:20 wb-lgc489196 Exp
 */
public class Invoker implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        try {
            return method.invoke(proxy, args);
        }catch (IllegalArgumentException e) {
            System.err.printf("please check parameters,methodName -> {%s} parameters ->  ",method.getName());
            Arrays.asList(args).forEach(v-> System.err.printf(" {%s} ",v));
            System.err.print("\n");
            return ERROR_CODE;
        }catch (IllegalAccessException e) {
            e.printStackTrace();
            return ERROR_CODE;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return ERROR_CODE;
        }

    }
}
