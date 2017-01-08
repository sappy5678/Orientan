/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.util.ArrayList;

/**
 *
 * @author zp
 */
public class ActionManager {

    ArrayList<MascotAction> actionList = new ArrayList<MascotAction>();

    public void add(MascotAction action) {
        actionList.add(action);
    }

    public void add(ArrayList<MascotAction> actionlist) {
        actionList.addAll(actionlist);
    }

    public void run() {

    }
}
