package com.laniakea.func;

import com.laniakea.regex.RegexHalf;
import com.laniakea.regex.RegexTrack;
import com.laniakea.router.InvokerInfo;
import com.laniakea.router.InvokerManager;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author wb-lgc489196
 * @version FuncProcessor.java, v 0.1 2019年08月20日 15:42 wb-lgc489196 Exp
 */
public class FuncProcessor implements Function {


    private InvokerManager invokerManager;

    private Object bean;

    private String expression;


    public static final String FUNC = "fun";

    private static final char COMMA = ',';

    private static final String SINGLE_MARK = "'";

    private static final char LEFT_PARENTHESES = '(';

    private static final char RIGHT_PARENTHESES = ')';

    private static final char SIGN_PARENTHESES = 'f';

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public FuncProcessor(FuncBulider funcBulider, InvokerManager invokerManager){
        this.bean = funcBulider.getObj();
        this.expression = funcBulider.getExpression();
        this.invokerManager = invokerManager;
    }


    public void resolveExpression(){

        if(StringUtils.isEmpty(expression) || !expression.contains(FUNC)){
            return;
        }

        expression.trim();


        RegexTrack regexTrack = RegexTrack.builder().source(expression).
                regexs(Arrays.asList(LEFT_PARENTHESES, RIGHT_PARENTHESES,SIGN_PARENTHESES)).bulid();

        List<Integer> markerIndex = regexTrack.getSeqIndex(SIGN_PARENTHESES);

        RegexHalf regexHalf = new RegexHalf(regexTrack.getSeqIndex(LEFT_PARENTHESES), regexTrack.getRevIndex(RIGHT_PARENTHESES));

        List<Integer> leftIndex = regexHalf.getLeftIndex();

        List<Integer> rightIndex = regexHalf.getRightIndex();


        IntStream.range(0,leftIndex.size()).boxed().forEach(i->{

            InvokerInfo InvokerInfo = new InvokerInfo();
            Integer left = leftIndex.get(i);
            String params = expression.substring(left + 1, rightIndex.get(i));

            if(params.contains(FUNC)){
                invokerManager.addFuncInvoker(InvokerInfo);
            }else{
                invokerManager.addTopInvoker(InvokerInfo);
            }

            String[] paramArrays = splitCondition(params, COMMA, LEFT_PARENTHESES, RIGHT_PARENTHESES);



            Object[] useParamArrays = new Object[paramArrays.length] ;
            List<Class<?>> paramTypes = new ArrayList<>();

            IntStream.range(0,paramArrays.length).boxed().forEach(convertTypeClass(useParamArrays,paramTypes,paramArrays,InvokerInfo));


            if(StringUtils.isEmpty(params)){
                throw new RuntimeException("params is not null ");
            }

            InvokerInfo.setParametersVal(useParamArrays);

            String methodName = expression.substring(markerIndex.get(i), left);

            InvokerInfo.setMethodName(methodName);
            InvokerInfo.setMethodNameAndParams(methodName + LEFT_PARENTHESES + params + RIGHT_PARENTHESES);
            Method method = ReflectionUtils.findMethod(bean.getClass(), methodName, (Class[]) paramTypes.toArray(new Class[paramTypes.size()]));
            InvokerInfo.setMethod(method);
            InvokerInfo.setBean(bean);
            InvokerInfo.setaClass(bean.getClass());
            InvokerInfo.setTypeParameters(method.getParameterTypes());
            invokerManager.addInvoker(InvokerInfo);
        });

    }

    public String[] splitCondition(String source, Character regex, Character condition1, Character condition2) {


        if(isIndexOf(source,condition1,condition2)){

            RegexTrack regexTrack = RegexTrack.builder().source(source)
                    .regexs(Arrays.asList(condition1, condition2, regex)).bulid();

            List<Integer> regexList = regexTrack.getSeqIndex(regex);
            List<Integer> leftList = regexTrack.getSeqIndex(condition1);
            List<Integer> rightList = regexTrack.getRevIndex(condition2);

            List<Integer> needIndex = new ArrayList<>();

            RegexHalf regexHalf = new RegexHalf(leftList, rightList);

            Map<Integer, Integer> indexMark = regexHalf.getIndexMark();

            OUT:for (Integer index : regexList) {
                IN:for (Map.Entry<Integer, Integer> entry : indexMark.entrySet()) {
                    if (entry.getKey() < index && index < entry.getValue()) {
                        continue OUT;
                    }
                }
                needIndex.add(index);
            }

            if (needIndex.size() == 0) {
                return source.split(",");
            }

            String[] indexArray = new String[needIndex.size() + 1];

            IntStream.range(0, needIndex.size() + 1).boxed().forEach(subParamters(indexArray,source,needIndex));

            return indexArray;

        }else{

            return source.split(",");
        }
    }

    private Consumer<Integer> convertTypeClass(Object[] useParamArrays,
                                               List<Class<?>> paramTypes,
                                               String[] paramArrays,
                                               InvokerInfo InvokerInfo) {
        return (b) -> {

            if (paramArrays[b].contains(FUNC)) {
                useParamArrays[b] = paramArrays[b];
                InvokerInfo.setSubFuncStr(paramArrays[b]);
            }
            if (!paramArrays[b].contains(SINGLE_MARK)) {
                if (!paramArrays[b].contains(FUNC)) {
                    useParamArrays[b] = Integer.valueOf(paramArrays[b]);
                }
                paramTypes.add(int.class);
            } else {
                useParamArrays[b] = paramArrays[b];
                paramTypes.add(String.class);
            }
        };
    }

    private Consumer<Integer> subParamters(String[] indexArray,
                                           String source,
                                           List<Integer> needIndex) {

        return (i) -> {
            if (i == 0) {
                indexArray[i] = source.substring(0, needIndex.get(i));
            }
            if (i != 0 && i != needIndex.size()) {
                indexArray[i] = source.substring(needIndex.get(i - 1) + 1, needIndex.get(i));
            }
            if (needIndex.size() == i) {
                indexArray[i] = source.substring(needIndex.get(i - 1) + 1);
            }
        };
    }

    private boolean isIndexOf(String source,Character condition1, Character condition2){
        return -1 != source.indexOf(condition1) && -1 != source.indexOf(condition2);
    }



    @Override
    public Object execute() throws Throwable {
        resolveExpression();
        return invokerManager.invoke();
    }
}
