/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan;

import java.util.ArrayList;

/**
 *
 * @author zp
 */
public final class MascotThreadNumberManager {

    private static int threadNumber = 0;

    public static int getThreadNumber() {
        return threadNumber;
    }

    public static void addOneThread() {
        threadNumber++;
    }

    public static void deleteOneThread() {
        threadNumber--;
    }

    public static Boolean isZero() {
        return threadNumber == 0;
    }
    /*
    private static ArrayList<Thread> manager=new ArrayList<Thread>();
    
    public static int getThreadNumber()
    {
        return manager.size();
    }
    public static void addThread(Thread th)
    {
        manager.add(th);
        manager.
    }
    public static Boolean haveNoThread()
    {
        return manager.size()==0;
    }
    /*
    public static void stopall()
    {
        for(int i=0;i<manager.size();i++)
        {
            
        }
    }*/

}
