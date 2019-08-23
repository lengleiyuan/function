package com.laniakea.router;

import com.laniakea.func.FuncBulider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author wb-lgc489196
 * @version InvokerManager.java, v 0.1 2019年08月16日 14:04 wb-lgc489196 Exp
 */
public class InvokerManager {

    private List<InvokerInfo> topInvoker = new ArrayList<>();

    private List<InvokerInfo> allInvoker = new ArrayList<>();

    private List<InvokerInfo> funcInvoker = new ArrayList<>();

    private List<InvokerInfo> topCritical = new ArrayList<>();

    private List<InvokerInfo> funcCritical = new ArrayList<>();

    private FuncBulider funcBulider;

    public InvokerManager(FuncBulider funcBulider) {
        this.funcBulider = funcBulider;
    }

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
        topCritical.add(InvokerInfo);
        return topCritical;
    }

    public List<InvokerInfo> addFuncInvoker(InvokerInfo InvokerInfo) {
        funcCritical.add(InvokerInfo);
        return funcCritical;
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

        topInvoker.addAll(topCritical);
        funcInvoker.addAll(funcCritical);

        Object res = null;

        for (InvokerInfo top : topInvoker) {

            res = invoker.invoke(top.getBean(), top.getMethod(), top.getParametersVal());
            top.setResult(res);

            topCritical.remove(top);
            for (InvokerInfo func : funcInvoker) {
                String subFuncStr = func.getSubFuncStr();
                if (!subFuncStr.contains(top.getMethodName())) {
                    continue;
                }
                funcCritical.remove(func);
                String resStr = String.valueOf(res);
                String newSubFuncStr = subFuncStr.replace(top.getMethodNameAndParams(), resStr);

                if (iscontainsFunc(newSubFuncStr)) {
                    funcBulider.setExpression(newSubFuncStr).getFuncProcessor().resolveExpression();
                    func.setSubFuncStr(newSubFuncStr);
                    Object[] parametersVal = func.getParametersVal();

                    IntStream.range(0, parametersVal.length).boxed().forEach(i -> {
                        String value = String.valueOf(parametersVal[i]);
                        if (value.equals(subFuncStr)) {
                            parametersVal[i] = newSubFuncStr;

                        }
                    });

                    funcCritical.add(func);

                } else {
                    Object[] parametersVal = func.getParametersVal();
                    Object[] parametersValnew = new Object[parametersVal.length];
                    boolean temp = false;
                    for (int i = 0; i < parametersVal.length; i++) {
                        String value = String.valueOf(parametersVal[i]);

                        if (value.equals(top.getMethodNameAndParams())) {

                            if (!resStr.contains(",")) {
                                parametersValnew[i] = Integer.valueOf(resStr);
                            } else {
                                parametersValnew[i] = resStr;
                            }

                        } else if(iscontainsFunc(value)) {
                            funcBulider.setExpression(value).getFuncProcessor().resolveExpression();
                            parametersValnew[i] = value;
                            temp = true;
                        }else {
                            if (!value.contains(",")) {
                                parametersValnew[i] = Integer.valueOf(value);
                            } else {
                                parametersValnew[i] = value;
                            }
                        }

                    }
                    func.setParametersVal(parametersValnew);
                    if(!temp){
                        topCritical.add(func);
                    }


                }
            }
            funcInvoker.clear();
        }
        topInvoker.clear();
        if (topCritical.isEmpty()) {
            return res;
        }
        return invoke();
    }

    public boolean iscontainsFunc(String funcStr) {
        if (!funcStr.contains("fun")) {
            return false;
        } else {
            return true;
        }
    }

}
