package grammar;

/**
 *
 * @author rkrantz
 */
public class CD {
    private int digit;
    public CD(String s) {
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
