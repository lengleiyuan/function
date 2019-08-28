package com.laniakea.func;

import com.laniakea.router.InvokerManager;

/**
 * @author wb-lgc489196
 * @version FuncBulider.java, v 0.1 2019年08月20日 15:40 wb-lgc489196 Exp
 */
public class FuncBulider {

    private String expression;

    private Object obj;

    private FuncProcessor funcProcessor;


    public FuncBulider() {}


    public FuncBulider(String expression) {
        this.expression = expression;
    }


    public static FuncBulider boxed(){
        return new FuncBulider();
    }

    public FuncProcessor bulid(){
        FuncProcessor funcProcessor = new FuncProcessor(this, new InvokerManager());
        this.funcProcessor = funcProcessor;
        return funcProcessor;
    }


    public String getExpression() {
        return expression;
    }

    public FuncBulider setExpression(String expression){
        this.expression = expression;
        return this;
    }

    public FuncBulider setBean(Object obj){
        this.obj = obj;
        return this;
    }

    public Object getObj() {
        return obj;
    }
}
