/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package virtualgusteau;

import java.util.*;

/**
 *
 * @author magnus
 */
public class Value {
    private int counter;
    private LinkedList<Integer> rIDs;

    public Value(int counter, int rID) {
        rIDs = new LinkedList<Integer>();
        this.counter = counter;
        rIDs.add(rID);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public LinkedList<Integer> getRIDs() {
        return rIDs;
    }

    public void setRIDs(LinkedList<Integer> rIDs) {
        this.rIDs = rIDs;
    }
    public void addRID(int id) {
        rIDs.add(id);
    }
    public void incrementCounter() {
        counter++;
    }
    
}
