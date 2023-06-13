package util;

import java.util.Random;

public class Randomizer {

    public static int getRandomNumberBetween(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }
}
