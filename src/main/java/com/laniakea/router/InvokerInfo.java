package com.laniakea.router;

import java.lang.reflect.Method;

/**
 * @author wb-lgc489196
 * @version InvokerBean.java, v 0.1 2019年08月16日 14:00 wb-lgc489196 Exp
 */
public class InvokerInfo {

    private Class<?> aClass;

    private Object bean;

    private Method method;

    private String methodName;

    private Class<?>[] typeParameters;

    private Object[] parametersVal;

    private int priority;

    private String subFuncStr;

    private String methodNameAndParams;

    public String getMethodNameAndParams() {
        return methodNameAndParams;
    }

    public void setMethodNameAndParams(String methodNameAndParams) {
        this.methodNameAndParams = methodNameAndParams;
    }

    private Object result;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSubFuncStr() {
        return subFuncStr;
    }

    public void setSubFuncStr(String subFuncStr) {
        this.subFuncStr = subFuncStr;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?>[] getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(Class<?>[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public Object[] getParametersVal() {
        return parametersVal;
    }

    public void setParametersVal(Object[] parametersVal) {
        this.parametersVal = parametersVal;
    }
}
