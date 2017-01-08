/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

/**
 *
 * @author zp
 */
public class ActionMode {

    public int getActionMode() {
        return ActionMode;
    }

    public void setActionMode(int ActionMode) {
        this.ActionMode = ActionMode;
    }

    public int getClimbMode() {
        //System.out.println("get: "+ClimbMode);
        return ClimbMode;
    }

    public void setClimbMode(int ClimbMode) {
        //System.out.println("SET: "+ClimbMode);
        this.ClimbMode = ClimbMode;//-1 is NoCimbMode, 1 is ClimbCeiling ,2 is Climb wall
        
    }
    private int ActionMode=0;//(0 is falling,1 is stand ,2 is sit)
    private int ClimbMode = -1;
}
