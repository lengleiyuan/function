package com.laniakea;

import com.laniakea.func.FuncBulider;

/**
 * @author wb-lgc489196
 * @version Test2019221.java, v 0.1 2019年08月21日 17:55 wb-lgc489196 Exp
 */
public class Test2019221 {


    public Object fun1(int b2,int b4){

        return b2 + b4;
    }
    public Object fun2(int b3,int b4){

        return b3 + b4;
    }

    public static void main(String[] args) throws Throwable {

        Test2019221 test2019221 = new Test2019221();
        Object execute = FuncBulider.boxed()
                .setExpression("fun1(3,fun2(2,fun1(1,8)))")
                .setBean(test2019221).bulid()
                .execute();

        System.out.println(execute);
    }
}
