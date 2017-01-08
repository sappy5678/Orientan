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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import orientan.Localelanguage;
import orientan.MascotThreadNumberManager;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author ASUS
 */
public class ImageSetChooser {

    private List<File> ImageFile = new ArrayList<File>();
    private String path = "";
    private Stage ImageChooserStage;
    private List<Image> ImageChoose = new ArrayList<Image>();
    private List<ImageView> ImageChooseView = new ArrayList<ImageView>();
    private String selectedPath = "";
    private HBox hboxButton = new HBox();
    //private Thread MainThread;

    public ImageSetChooser() {
        ImageChooserStage = new Stage();
        //MainThread = mainThread;
    }

    public String getPath() {
        return path;
    }

    public void run() {

        int i = 0;
        for (File f : new File(System.getProperty("user.dir") + "\\img").listFiles()) {
            
            if (f.getName().equals("icon")) {
                continue;
            }
            
            ImageFile.add(f);
            Image tempimage = new Image("file:///" + ImageFile.get(i).getPath() + "\\shime1.png");
            ImageChoose.add(tempimage);
            ImageView tempview = new ImageView(ImageChoose.get(i));
            ImageChooseView.add(tempview);
            //System.out.println(f);
            i++;

        }
        if (i == 0) {
            final Alert alert = new Alert(AlertType.WARNING); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
            alert.setTitle("Warning: Nothing in the img"); //設定對話框視窗的標題列文字
            alert.setHeaderText("How to fix?"); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
            alert.setContentText("Put a folder content your img(shime image),and named the folder with the character's name"); //設定對話框的訊息文字
            alert.showAndWait(); //顯示對話框，並等待對話框被關閉時才繼續執行之後的程式
            System.exit(0);
        }
        /*Image first = new Image("file:///" + ImageFile.get(3).getPath() + "\\shime1.png");
        ImageView iv1 = new ImageView(first);

        Image second = new Image("file:///" + ImageFile.get(4).getPath() + "\\shime1.png");
        ImageView iv2 = new ImageView(second);*/
        //
        ListView<String> listView = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int j = 0; j < ImageFile.size(); j++) {
            items.add(ImageFile.get(j).getName().toString());
        }
        listView.setItems(items);
        listView.setCellFactory(param -> new ListCell<String>() {
            //private ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    for (int k = 0; k < ImageFile.size(); k++) {
                        if (name.equals(ImageFile.get(k).getName().toString())) {
                            setText(name);
                            this.setFont(Font.font(20));
                            setGraphic(ImageChooseView.get(k));
                        }
                    }
                }
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                System.out.println(new_val);
                for (int h = 0; h < ImageFile.size(); h++) {
                    if (new_val.equals(ImageFile.get(h).getName().toString())) {
                        selectedPath = ImageFile.get(h).getPath();
                    }
                }

            }
        });
        VBox box = new VBox(listView);
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(listView, Priority.ALWAYS);
        //Scene scene = new Scene(box, 200, 200);
        //ImageChooserStage.setScene(scene);
        //ImageChooserStage.showAndWait();
        //
        /*
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefSize(115, 700);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        ToggleGroup group = new ToggleGroup();
        RadioButton[] choose = new RadioButton[ImageFile.size()];
        for (int j = 0; j < ImageFile.size(); j++) {
            choose[j] = new RadioButton(ImageFile.get(j).getName().toString());
            choose[j].setUserData(ImageFile.get(j).getPath());
            choose[j].setSelected(false);
            choose[j].setToggleGroup(group);
            choose[j].setMinSize(10, 20);
        }
        /*
        //預設選項
        choose[0].setSelected(true);
        choose[0].requestFocus();
        //RadioButton[] choose = new RadioButton[2];
        /*RadioButton chose_one = new RadioButton(ImageFile.get(3).getName().toString());
        chose_one.setUserData(ImageFile.get(3).getPath());
        chose_one.setSelected(false);
        chose_one.setToggleGroup(group);
        chose_one.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                System.out.println("chose one chose");
            }

        });
        chose_one.setMinSize(10, 20);
        RadioButton chose_two = new RadioButton(ImageFile.get(4).getName().toString());
        chose_two.setUserData(ImageFile.get(4).getPath());
        chose_two.setToggleGroup(group);
        chose_two.setSelected(false);
        chose_two.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                System.out.println("chose two chose");
            }

        });
        chose_two.setMinSize(10, 20);
        //決定是哪一隻角色
         */
        //設定關閉設窗就關閉程式
        ImageChooserStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                if(MascotThreadNumberManager.getThreadNumber()==1)
                {
                    System.exit(0);
                }           
            }
        });

        Button set = new Button(Localelanguage.getStringLocalelanguage("Set"));
        set.setFont(Font.font(30));
        set.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //path = group.getSelectedToggle().getUserData().toString();
                path=selectedPath;
                System.out.println("set character");
                ImageChooserStage.close();
                //inTask.notify();
            }
        });
        /*
        group.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle,
                        Toggle new_toggle) -> {
                    if (group.getSelectedToggle() != null) {
                        System.out.println(group.getSelectedToggle().getUserData());
                    }
                });
        VBox vb = new VBox();

        for (int k = 0; k < choose.length; k++) {
            HBox temphb = new HBox();
            temphb.getChildren().addAll(choose[k], ImageChooseView.get(k));
            vb.getChildren().add(temphb);

        }


        /*choose[0] = chose_one;
        choose[1] = chose_two;*/
        //StackPane root = new StackPane();  
        /*vb.getChildren().addAll(chose_one, chose_two);
        vb.getChildren().add(set);*/
        //vb.getChildren().add(set);
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefSize(115, 700);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        //scroll.setContent(box);
        //VBox scenebox = new VBox();
        //scenebox.getChildren().addAll(scroll);
        HBox.setHgrow(set, Priority.ALWAYS);
        set.setMaxWidth(Double.MAX_VALUE);
        hboxButton.getChildren().add(set);
        BorderPane borderMain = new BorderPane();
        borderMain.setCenter(box);
        borderMain.setBottom(set);
        set.setAlignment(Pos.CENTER);
        Scene sc = new Scene(borderMain, Screen.getPrimary().getVisualBounds().getWidth()*2/3,Screen.getPrimary().getVisualBounds().getHeight()*2/3);
        BorderPane.setAlignment(hboxButton, Pos.CENTER);
        ImageChooserStage.setX(Screen.getPrimary().getVisualBounds().getWidth()/5);
        ImageChooserStage.setY(Screen.getPrimary().getVisualBounds().getHeight()/5);
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
