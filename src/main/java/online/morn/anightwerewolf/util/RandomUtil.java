package online.morn.anightwerewolf.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 随机工具
 * @auther Horner 2017/11/26 23:53
 */
public class RandomUtil {

    /**
     * 生成随机数（范围 0 ~ 给定值-1）
     * @auther Horner 2017/11/27 0:15
     * @param count
     * @return
     */
    public static int generateRandom(int count){
        return (int) (Math.random() * count);
    }

    /**
     * 生成随机数列表 (数组样例：[0,2,1,3,4])
     * @auther Horner 2017/11/27 0:14
     * @param listSize
     * @return
     */
    public static List<Integer> generateRandomList(int listSize){
        List<Integer> numberList = new ArrayList<Integer>();
        for(int i = 0 ; i < listSize ; i++){
            numberList.add(i);
        }
        List<Integer> randomList = new ArrayList<Integer>();
        for(int i = listSize ; i > 0 ; i--){
            int index = RandomUtil.generateRandom(i);
            randomList.add(numberList.get(index));
            numberList.remove(index);
        }
        return randomList;
    }

    /*public static void main(String[] args){
        for(Integer i : generateRandomList(5)){
            System.out.println("---------" + i);
        }
    }*/
}
