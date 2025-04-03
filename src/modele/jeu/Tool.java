package modele.jeu;

import java.util.Random;

public class Tool {
    private static final Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }
}

