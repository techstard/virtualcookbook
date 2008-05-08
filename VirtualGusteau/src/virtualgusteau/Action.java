package virtualgusteau;

public class Action {
    private boolean negation = false;
    private String name;
    private Target target;

    public Action(String name, Target target) {
        this.name = name;
        this.target = target;
    }

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNegation() {
        return negation;
    }

    public void setNegation(boolean negation) {
        this.negation = negation;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
    
    
}