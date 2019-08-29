package com.laniakea;

import com.laniakea.func.FuncBulider;

/**
 * @author wb-lgc489196
 * @version Test2019221.java, v 0.1 2019年08月21日 17:55 wb-lgc489196 Exp
 */
public class Test2019221 {

    public class FuncMethod{

        public Integer fun1(int b2,int b4,int b1){

            return b2 + b4 + b1;
        }
        public Integer fun2(int b3,int b4){

            return b3 + b4;
        }
        public Integer fun3(int b3,int b4){

            return b3 + b4;
        }

    }



    public static void main(String[] args) {

        Test2019221 test2019221 = new Test2019221();
        FuncMethod funcMethod = test2019221.new FuncMethod();
        FuncBulider.boxed().setExpression("fun1(0,fun2(5,fun1(1,fun2(56,4),7)),fun3(5,0))")
                .setBean(funcMethod).bulid()
                .execute();

    }
}
