/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import orientan.ImageSetChooser.ImageSetChooser;
import orientan.config.loadconfig;
import orientan.mascot.mascot;
import orientan.mascotEnvironment.Mouse;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author zp
 */
public class AddMascotService extends Thread {

    private Mouse mouseDetect;
    private loadconfig config;
    private String Path;
   // private List<File> ImageFile = new ArrayList<File>();
    //private String path = "";
   // private Stage ImageChooserStage=new Stage();
    private ImageSetChooser imageChooser=new ImageSetChooser();
    public AddMascotService(Mouse Inmouse, loadconfig Inconfig,String imgPath) {
        this.mouseDetect = Inmouse;
        this.config = Inconfig;
        Path=imgPath;
    }
    @Override
    public void run() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                imageChooser.run();
                Path=imageChooser.getPath();
                mascotenvironment.setImage(new Image(new File(Path+ "\\shime1.png").toURI().toString()));
                mascot m = new mascot(config, mouseDetect,Path);
            }
        });
    }
    public String returnPath()
    {
        return Path;
    }

}
