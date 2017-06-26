package com.nbicc.utils;
import java.util.Random;
/**
 * Created by 大泥藕 on 2017/6/14.
 */
public class MathUtil {

    public static int getRandomInt(int min,int max){

        if (min==max||(min-max>0)){
            return 0;
        }
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;

    }
}
