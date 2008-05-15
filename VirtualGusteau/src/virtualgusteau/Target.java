/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

/**
 *
 * @author magnus
 */
public class Target {
    private String name;
    private Target subTarget;
    private int numberOfPeople;
    private boolean negation;

    public Target(String name, Target subTarget) {
        this.name = name;
        this.subTarget = subTarget;
    }

    public Target(String name) {
        this.name = name;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public boolean isNegation() {
        return negation;
    }

    public void setNegation(boolean negation) {
        this.negation = negation;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Target getSubTarget() {
        return subTarget;
    }

    public void setSubTarget(Target subTarget) {
        this.subTarget = subTarget;
    }
    @Override
    public String toString() {
        if(subTarget instanceof Target) {
            return name+"("+subTarget+")";
        } else {
            return name;
        }
    }
    
}
