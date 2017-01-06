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

    public Boolean getClimbMode() {
        return ClimbMode;
    }

    public void setClimbMode(Boolean ClimbMode) {
        this.ClimbMode = ClimbMode;
    }
    private int ActionMode=0;//(0 is falling,1 is stand ,2 is sit)
    private Boolean ClimbMode = false;
}
