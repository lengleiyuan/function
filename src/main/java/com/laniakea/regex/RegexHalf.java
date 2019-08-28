package com.laniakea.regex;



import java.util.*;

/**
 * @author wb-lgc489196
 * @version RegexHalf.java, v 0.1 2019年08月19日 10:36 wb-lgc489196 Exp
 */
public class RegexHalf {


    private List<Integer> leftIndex;

    private List<Integer> rightIndex;

    private Map<Integer, Integer> indexMark = new LinkedHashMap<>();

    public RegexHalf(List<Integer> leftIndex, List<Integer> rightIndex) {
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        init();
    }

    public void init() {

        Set<Integer> removeIndex = new HashSet<>();

        List<Integer> filterIndex = new ArrayList<>();

        HashMap<Integer, Integer> tempMark = new HashMap<>();

        filterIndex.addAll(rightIndex);

        OUT:
        for (int i = 0; i < leftIndex.size(); i++) {

            //初始化变量
            int tepm = Integer.MAX_VALUE;
            IN:
            for (int j = 0; j < rightIndex.size(); j++) {

                int temp1 = Math.abs(leftIndex.get(i) - rightIndex.get(j));

                //绝对值小的进入
                if (tepm > temp1) {

                    //添加i轮绝对值
                    tempMark.put(i, temp1);
                    tepm = temp1;
                    if (i > 0) {
                        SUB:
                        for (int b = 0; b < i; b++) {

                            //b轮right值
                            int tepm5 = indexMark.get(leftIndex.get(b));

                            //b轮值不是本轮rightIndex 不操作
                            //满足本轮rightIndex才需要判断替换
                            if (tepm5 != rightIndex.get(j)) {
                                continue SUB;
                            }
                            //查找每一轮的绝对值
                            int temp2 = tempMark.get(b);

                            //获取优先级,SUB优先级高于,b轮需要替换
                            if (temp2 > tepm) {

                                //清空链表
                                filterIndex.clear();
                                //加入全部元素
                                filterIndex.addAll(rightIndex);
                                //加入排除元素
                                filterIndex.removeAll(removeIndex);

                                int tepm4 = Integer.MAX_VALUE;

                                //遍历替换合适的元素
                                INSUB:
                                for (int c = 0; c < filterIndex.size(); c++) {
                                    //b轮left与c轮right值绝对值
                                    int tepm3 = Math.abs(leftIndex.get(b) - filterIndex.get(c));
                                    // 重新开始比较
                                    if (tepm4 > tepm3) {
                                        tepm4 = tepm3;
                                        indexMark.put(leftIndex.get(b), filterIndex.get(c));
                                        removeIndex.remove((Object) tepm5);
                                        tempMark.put(b, Math.abs(leftIndex.get(b) - filterIndex.get(c)));
                                    }
                                }

                            }
                            //若小于或相等,i轮不能用b轮的值
                            if (temp2 <= tepm) {
                                tepm = Integer.MAX_VALUE;
                                continue IN;
                            }
                        }
                    }
                    indexMark.put(leftIndex.get(i), rightIndex.get(j));
                }

            }
            removeIndex.addAll(indexMark.values());
        }


        rightIndex.clear(); leftIndex.clear();

        for (Map.Entry<Integer, Integer> entry : indexMark.entrySet()) {
            leftIndex.add(entry.getKey());
            rightIndex.add(entry.getValue());
        }
    }




    public Map<Integer, Integer> getIndexMark() {
        return indexMark;
    }

    public List<Integer> getLeftIndex() {
        return leftIndex;
    }

    public List<Integer> getRightIndex() {
        return rightIndex;
    }
}
