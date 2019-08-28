package com.laniakea.router;

import com.laniakea.func.FuncBulider;
import com.laniakea.regex.RegexHalf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wb-lgc489196
 * @version InvokerManager.java, v 0.1 2019年08月16日 14:04 wb-lgc489196 Exp
 */
public class InvokerManager {

    private List<InvokerInfo> topInvoker = new ArrayList<>();

    private List<InvokerInfo> allInvoker = new ArrayList<>();

    private List<InvokerInfo> funcInvoker = new ArrayList<>();

    private List<InvokerInfo> topCritical = new LinkedList<>();

    private List<InvokerInfo> funcCritical = new LinkedList<>();


    private Invoker invoker = new Invoker();

    public List<InvokerInfo> getFuncInvoker() {
        return funcInvoker;
    }

    public void setFuncInvoker(List<InvokerInfo> funcInvoker) {
        this.funcInvoker = funcInvoker;
    }

    public List<InvokerInfo> addInvoker(InvokerInfo InvokerInfo) {
        allInvoker.add(InvokerInfo);
        return allInvoker;
    }

    public List<InvokerInfo> addTopInvoker(InvokerInfo InvokerInfo) {
        topInvoker.add(InvokerInfo);
        return topInvoker;
    }

    public List<InvokerInfo> addFuncInvoker(InvokerInfo InvokerInfo) {
        funcInvoker.add(InvokerInfo);
        return funcInvoker;
    }

    public List<InvokerInfo> getTopInvoker() {
        return topInvoker;
    }

    public void setTopInvoker(List<InvokerInfo> topInvoker) {
        this.topInvoker = topInvoker;
    }

    public List<InvokerInfo> getAllInvoker() {
        return allInvoker;
    }

    public void setAllInvoker(List<InvokerInfo> allInvoker) {
        this.allInvoker = allInvoker;
    }

    public Object invoke() throws Throwable {

        Object res = null;

        for (InvokerInfo top : topInvoker) {

            topCritical.remove(top);

            res = invoker.invoke(top.getBean(), top.getMethod(), top.getParametersVal());

            top.setResult(res);

            String resStr = String.valueOf(res);

            for (InvokerInfo func : funcInvoker) {

                funcCritical.remove(func);

                String methodNameAndParams = func.getMethodNameAndParams();

                func.setMethodNameAndParams(methodNameAndParams.replace(top.getMethodNameAndParams(), resStr));

                Object[] parametersVal = func.getParametersVal();

                boolean isTemp = false;

                for (int i = 0; i < parametersVal.length; i++) {

                    String value = String.valueOf(parametersVal[i]);

                    value = value.replace(top.getMethodNameAndParams(), resStr);

                    if (RegexHalf.isNumeric(value)) {
                        parametersVal[i] = Integer.valueOf(value);
                    } else {
                        parametersVal[i] = value;
                    }
                    if(RegexHalf.iscontainsFunc(value)){
                        isTemp = true;
                    }

                }

                if(isTemp){
                    System.out.printf("add new funcInvoker chain,func methodNameAndParams: %s.\n",func.getMethodNameAndParams());
                    funcCritical.add(func);
                }else{
                    System.out.printf("add new topInvoker chain ,func methodNameAndParams: %s.\n",func.getMethodNameAndParams());
                    topCritical.add(func);
                }

            }

            funcInvoker.clear();
            funcInvoker.addAll(funcCritical);
        }

        topInvoker.clear();
        topInvoker.addAll(topCritical);

        if (topCritical.isEmpty()) {
            System.out.println("The end execution result set is { " + res + " } ");
            return res;
        }

        return invoke();
    }


}
