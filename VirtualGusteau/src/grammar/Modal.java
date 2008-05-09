package grammar;

/**
 *
 * @author rkrantz
 */
public class Modal {
    private String modal;
    public Modal(String s) {
        modal = s;
    }

    @Override
    public String toString() {
        return "Modal:"+modal;
    }

    public String getModal() {
        return modal;
    }
    
}
