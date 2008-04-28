/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package grammar;

/**
 *
 * @author rkrantz
 */
public class TreeNode {
    private Object value;
    private TreeNode left = null;
    private TreeNode right = null;
    
    public TreeNode(){}
    
    public TreeNode(Object initValue, TreeNode initLeft, TreeNode initRight)
    {
        value = initValue;
        left = initLeft;
        right = initRight;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    public TreeNode getLeft()
    {
        return left;
    }
    
    public TreeNode getRight()
    {
        return right;
    }
    
    public void setLeft(TreeNode theNewLeft)
    {
        left = theNewLeft;
    }
    
    public void setRight(TreeNode theNewRight)
    {
        right = theNewRight;
    }
    public boolean isEndNode() {
        if(left == null && right == null) {
            return true;
        } else return false;
    }
}
