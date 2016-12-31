/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.ImageSetChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static javafx.application.Application.launch;

/**
 *
 * @author ASUS
 */
public class ImageSetChooser {

    private List<File> imageurl=new ArrayList<File>();

    public ImageSetChooser() {
        int i = 0;
        for (File f : new File(System.getProperty("user.dir") + "\\img").listFiles()) {
            imageurl.add(f);
            System.out.println(imageurl.get(i));
            i++;
        }
        //4、5為圖包資料夾
    }
    public static void main(String[] args) {
        ImageSetChooser chooser=new ImageSetChooser();
    }
}
