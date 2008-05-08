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

    public Target(String name, Target subTarget) {
        this.name = name;
        this.subTarget = subTarget;
    }

    public Target(String name) {
        this.name = name;
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
    
    
}
