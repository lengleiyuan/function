package com.laniakea.regex;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wb-lgc489196
 * @version RegexTrack.java, v 0.1 2019年08月19日 10:22 wb-lgc489196 Exp
 */
public class RegexTrack {


    private RegexTrack(List<Character> regexs) {
        this.regexs = regexs;
    }
    private List<Character> regexs;

    private int size;

    private Map<Character,List<Integer>>  charListMap = new HashMap<>();

    private RegexTrack(Builder builder) {

        this(builder.regexs);

        if(builder.consumerList != null){
            fillConsumerList(builder.source, builder.consumerList);
        }else if(builder.consumerMap  != null){
            fillConsumerMap(builder.source, builder.consumerMap);
        }else {
            fill(builder.source);
        }
    }

    public int getSize() {
        return size;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<Character, List<Integer>> getCharListMap() {
        return charListMap;
    }

    public List<Integer> getSeqIndex(Character c){
        List<Integer> integerList = charListMap.get(c);
        if(null == integerList){
            throw new RuntimeException("this regex is not found list");
        }
        return integerList;
    }


    public List<Integer> getRevIndex(Character c){
        List<Integer> integerList = charListMap.get(c);
        if(null == integerList){
            throw new RuntimeException("this regex is not found list");
        }

        return  integerList.stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList());
    }

    public List<Integer> getSeqIndex(){
        return charListMap.get(regexs.get(0));
    }


    public List<Integer> getRevIndex(){
        List<Integer> integerList = charListMap.get(regexs.get(0));

        return    integerList.stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList());
    }


    private void fill(String source) {
        for (Character regex : regexs) {
            List<Integer> indexList = new ArrayList<>();
            IntStream.range(0, source.length()).boxed().forEach(i -> {
                if (source.charAt(i) == regex) {
                    indexList.add(i);
                    charListMap.put(regex, indexList);
                }
            });
        }
        this.size = charListMap.size();

    }

    private void fillConsumerList(String source, BiConsumer<List<Integer>, Integer> consumer) {

        if (null == consumer) {
            fill(source);
            return;
        }

        IntStream.range(0, source.length()).boxed().forEach(i -> consumer.accept(charListMap.get(regexs.get(0)), i));
    }

    private void fillConsumerMap(String source, BiConsumer<Map<Character,List<Integer>>, Integer> consumer) {

        if (null == consumer) {
            fill(source);
            return;
        }

        IntStream.range(0, source.length()).boxed().forEach(i -> consumer.accept(charListMap, i));

    }

    public static class Builder {

        private List<Character> regexs;

        private String source;

        private BiConsumer<List<Integer>, Integer> consumerList;

        private BiConsumer<Map<Character,List<Integer>>, Integer> consumerMap;

        public Builder regex(Character regex) {
            if(null == regexs){
                regexs = new ArrayList<>();
            }
            regexs.add(regex);
            return this;
        }

        public Builder regexs(List<Character> regexs) {
            this.regexs = regexs;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder consumerList(BiConsumer<List<Integer>, Integer> consumerList) {

            this.consumerList = consumerList;
            return this;
        }
        public Builder consumerMap( BiConsumer<Map<Character,List<Integer>>, Integer> consumerMap) {

            this.consumerMap = consumerMap;
            return this;
        }

        public RegexTrack bulid() {
            if(null != consumerList && null != consumerMap ){
                throw new RuntimeException("Only one type can be consumed");
            }
            return new RegexTrack(this);
        }
    }

}
