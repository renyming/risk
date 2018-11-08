package common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Card Enums for card exchange.
 */
public enum CardType {

    /**
     * The @INFANTRY @CAVALRY @ARTILLERY card enums.
     */
    INFANTRY, CAVALRY, ARTILLERY;

    private static final List<CardType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static CardType randomLetter()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
