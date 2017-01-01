/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.ImageSetChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author ASUS
 */
public class ImageSetChooser {

    private List<File> ImageFile = new ArrayList<File>();
    private String path = "";
    private Stage ImageChooserStage;
    //private Thread MainThread;

    public ImageSetChooser() {
        ImageChooserStage = new Stage();
        //MainThread = mainThread;
    }

    public String getPath() {
        return path;
    }

    public void run() {

        for (File f : new File(System.getProperty("user.dir") + "\\img").listFiles()) {
            ImageFile.add(f);
            System.out.println(f);

        }
        Image first = new Image("file:///" + ImageFile.get(3).getPath() + "\\shime1.png");
        ImageView iv1 = new ImageView(first);

        Image second = new Image("file:///" + ImageFile.get(4).getPath() + "\\shime1.png");
        ImageView iv2 = new ImageView(second);
        ToggleGroup group = new ToggleGroup();
        //RadioButton[] choose = new RadioButton[2];
        RadioButton chose_one = new RadioButton(ImageFile.get(3).getName().toString());
        chose_one.setUserData(ImageFile.get(3).getPath());
        chose_one.setSelected(false);
        chose_one.setToggleGroup(group);
        /*chose_one.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                System.out.println("chose one chose");
            }

        });*/
        chose_one.setMinSize(10, 20);
        RadioButton chose_two = new RadioButton(ImageFile.get(4).getName().toString());
        chose_two.setUserData(ImageFile.get(4).getPath());
        chose_two.setToggleGroup(group);
        /*chose_two.setSelected(false);
        chose_two.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                System.out.println("chose two chose");
            }

        });*/
        chose_two.setMinSize(10, 20);
        //決定是哪一隻角色
        Button set = new Button("Set");
        set.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                path = group.getSelectedToggle().getUserData().toString();
                System.out.println("set character");
                ImageChooserStage.close();
                //inTask.notify();
            }
        });
        group.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle,
                        Toggle new_toggle) -> {
                    if (group.getSelectedToggle() != null) {
                        System.out.println(group.getSelectedToggle().getUserData());
                    }
                });
        HBox hb1 = new HBox();
        HBox hb2 = new HBox();
        hb1.getChildren().addAll(chose_one, iv1);
        hb2.getChildren().addAll(chose_two, iv2);

        /*choose[0] = chose_one;
        choose[1] = chose_two;*/
        //StackPane root = new StackPane();  
        VBox vb = new VBox();
        /*vb.getChildren().addAll(chose_one, chose_two);
        vb.getChildren().add(set);*/
        vb.getChildren().addAll(hb1, hb2, set);
        Scene sc = new Scene(vb, 300, 300);
        ImageChooserStage.setX(200);
        ImageChooserStage.setY(200);
        ImageChooserStage.setTitle("chose");
        ImageChooserStage.setScene(sc);
        ImageChooserStage.showAndWait();
    }
}

/*public static void main(String[] args) {
        launch(args);
        //System.out.println("chose two chose");
    }*/
 /*public ImageSetChooser() {
        for (File f : new File(System.getProperty("user.dir") + "\\img").listFiles()) {
            ImageFile.add(f);
            System.out.println(f);
        }
        CheckBox[] choose = new CheckBox[2];
        CheckBox chose_one = new CheckBox(ImageFile.get(3).getName().toString());
        
        chose_one.setSelected(false);
        chose_one.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                System.out.println("chose one chose");
            }

        });
        CheckBox chose_two = new CheckBox(ImageFile.get(4).getName().toString());
        chose_two.setSelected(false);
        chose_two.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                System.out.println("chose two chose");
            }

        });
        choose[0] = chose_one;
        choose[1] = chose_two;
    }*/
