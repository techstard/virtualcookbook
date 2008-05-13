package grammar;

/**
 *
 * @author rkrantz
 */
public class Digit {
    private int digit;
    public Digit(String s) {
        digit = Integer.parseInt(s);
    }

    @Override
    public String toString() {
        return "Digit:"+digit;
    }

    public int getDigit() {
        return digit;
    }
}
