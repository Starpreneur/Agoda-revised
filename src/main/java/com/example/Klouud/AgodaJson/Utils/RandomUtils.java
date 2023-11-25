package com.example.Klouud.AgodaJson.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RandomUtils {

    public static int DEFAULT_LENGTH = 39;

    public static String randomAlphaNumeric(Integer length){

        List<Character> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            char[] chars = UUID.randomUUID().toString().replaceAll("-","").toCharArray();
            for(char aChar: chars){
                list.add(aChar);
            }
        }
        StringBuilder builder = new StringBuilder();
        Collections.shuffle(list);
        length = length == null ? DEFAULT_LENGTH : length;
        for(int i = 0;i<length;i++){
            builder.append(list.get(i));
        }

        return builder.toString();
    }


}
