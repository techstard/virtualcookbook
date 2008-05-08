package grammar;

/**
 *
 * @author rkrantz
 */
public class Gerund {
    private String gerund;
    public Gerund(String s) {
        gerund = s;
    }

    @Override
    public String toString() {
        return "Gerund:"+gerund;
    }

    public String getGerund() {
        return gerund;
    }    
}
