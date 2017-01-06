package orientan.mascot;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import orientan.config.Action;
import orientan.config.loadconfig;
import orientan.mascotEnvironment.Mouse;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author zp
 */
public class mascot {

    private String imagePath;
    private TimelineManger animationManger;
    private loadconfig configList;
    private Stage mascotStage = new Stage();
    private Image DefaultStandImage;
    private Image DefaultSitImage;
    private ImageView MascotimageView = new ImageView(DefaultStandImage);
    private Walk walkAction;
    private Run runAction;
    private Dash dashAction;
    private FallingAndBouncing fallAction;
    private Drag dragAction;
    private static int ActionMode = 0; //(0 is falling,1 is stand ,2 is sit)
    private Boolean ClimbMode = false;
    private ArrayList<MascotAction> actionList = new ArrayList<MascotAction>();
    private Random random = new Random();
    private boolean isAction = false;
    //private double mascotdeltaX = 0.5;
    //private double mascotdeltaY = 0.02;
    //private Time currentTime;

    public mascot(loadconfig actionConfig, Mouse mouseDetect, String imgPath) {
        //初始化設定
        this.configList = actionConfig;
        //設定視窗初始位置
        this.imagePath = imgPath;
        this.animationManger = new TimelineManger(imagePath);
        this.DefaultStandImage = new Image(new File(imagePath + "\\shime1.png").toURI().toString());
        DefaultSitImage = new Image(new File(imagePath + "/shime11.png").toURI().toString());
        mascotStage.setY(mascotenvironment.getFloor());
        mascotStage.setX(mascotenvironment.getRightWall() - 10);
        walkAction = new Walk(mascotStage, MascotimageView, configList.getData("Walk", "Move"), animationManger, imagePath);
        runAction = new Run(mascotStage, MascotimageView, configList.getData("Run", "Move"), animationManger, imagePath);
        dashAction = new Dash(mascotStage, MascotimageView, configList.getData("Dash", "Move"), animationManger, imagePath);
        fallAction = new FallingAndBouncing(mascotStage, MascotimageView, configList.getFallingData(), configList.getData("Bouncing", "Animate"), configList.getData("Jumping", "Embedded"), animationManger, isAction, imagePath);
        dragAction = new Drag(mascotStage, MascotimageView, configList.getData("Resisting", "Embedded"), animationManger, imagePath);
        actionList.add(walkAction);
        actionList.add(runAction);
        actionList.add(dashAction);
        //開始的random
        //actionList.get(random.nextInt(3)).play(random.nextInt(20) + 1);
        mascotStage.setY(-500);
        mascotStage.setX(random.nextInt((int) mascotenvironment.getRightWall()) + mascotenvironment.getLeftWall());
        fallAction.setNoCeiling(true);
        fallAction.Falling(0, 0);
        fallAction.setNoCeiling(true);
        //System.out.println(configList.getData("Resisting", "Embedded").getAnimation().size());
        //deltaX=Walk.getAnimation().get(0).getVelocity();
        /*按鈕測試
        javafx.scene.control.Button btn = new javafx.scene.control.Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
         */
 /**/
        //設定透明顯示視窗
        //stage透明化   
        mascotStage.setAlwaysOnTop(true);   //讓其永遠在最上層
        mascotStage.initStyle(StageStyle.TRANSPARENT);
        AnchorPane root = new AnchorPane();
        //設定root顏色
        //root.getChildren().add(btn);
        root.setStyle("-fx-background:transparent;");
        //設定scene顏色與大小
        Scene scene = new Scene(root, mascotenvironment.getImageWidth(), mascotenvironment.getImageHeight());
        scene.setFill(null);

        mascotStage.setScene(scene);
        mascotStage.show();
        /*事件HANDLE*/
        //設定拖曳圖片
        scene.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //暫停動作
                System.out.println(ActionMode);
                /*
                if (!ActionMode.equals("falling")) {
                    animationManger.StopAll();
                    if(ActionMode.equals("stand"))     
                        MascotimageView.setImage(DefaultStandImage);
                    else if(ActionMode.equals("sit"))
                        MascotimageView.setImage(DefaultSitImage);
                 
                }   */
                MascotimageView.setImage(DefaultStandImage);
                me.consume();
            }
        });
        scene.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {

                if (mascotStage.getY() == mascotenvironment.getFloor()) {
                    isAction = false;
                }
                if (!isAction) {
                    actionList.get(random.nextInt(3)).play(random.nextInt(20) + 1);
                }
                me.consume();
            }
        });
        scene.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                isAction = true;
                /* drag was detected, start drag-and-drop gesture*/
                //System.out.println("onDragDetected");
                /* allow MOVE transfer mode */
                if (me.getButton() != MouseButton.MIDDLE && me.getButton() != MouseButton.SECONDARY) {
                    animationManger.StopAll();
                }
                me.consume();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                /* data is dragged over the target */
                if (mouseEvent.getButton() != MouseButton.MIDDLE && mouseEvent.getButton() != MouseButton.SECONDARY) {
                    mouseDetect.updateMouseData(mouseEvent);
                    dragAction.ResistingAndDrag(mouseDetect);
                    mascotStage.setX(mouseEvent.getScreenX() - mascotenvironment.getImageWidth() / 2);
                    mascotStage.setY(mouseEvent.getScreenY() - mascotenvironment.getImageHeight() / 5);
                }
                mouseEvent.consume();
            }

        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEventdrop) {
                if (mouseEventdrop.getButton() != MouseButton.MIDDLE && mouseEventdrop.getButton() != MouseButton.SECONDARY) {
                    animationManger.StopAll();
                    fallAction.Falling(mouseDetect.getMouseSpeedX(), mouseDetect.getMouseSpeedY());
                    //isAction=false;
                }
            }
        });
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEventright) {
                if (mouseEventright.getButton() == MouseButton.SECONDARY) {
                    MenuItem webcommand = new MenuItem();
                    webcommand.setId("Web command");
                    webcommand.setText("Web Command");
                    MenuItem exit = new MenuItem();
                    exit.setId("Exit");
                    exit.setText("Exit");
                    ContextMenu contextmenu = new ContextMenu(webcommand, exit);
                    contextmenu.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            switch (((MenuItem) event.getTarget()).getId()) {
                                case "Web command":
                                    System.out.println("web");
                                    Stage WebRecom = new Stage();
                                    ScrollPane scroll = new ScrollPane();
                                    scroll.setPrefSize(115, 700);
                                    scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                                    scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                                    //StackPane Webroot = new StackPane();
                                    VBox vbox = new VBox();
                                    VBox box = new VBox();
                                    vbox.setMinSize(700, 700);
                                    box.setMinSize(600, 600);
                                    Scene WebRe = new Scene(vbox);
                                    Hyperlink[] link = new Hyperlink[2];
                                    String[] linkname = new String[2];
                                    linkname[0] = "Youtube";
                                    linkname[1] = "Yahoo";
                                    final String[] url = new String[2];
                                    url[0] = "https://www.youtube.com/?gl=TW&hl=zh-TW";
                                    url[1] = "https://tw.yahoo.com/";
                                    for (int i = 0; i < link.length; i++) {
                                        Hyperlink templink = link[i] = new Hyperlink(linkname[i]);
                                        templink.setFont(Font.font("Arial", 20));
                                        String tempurl = url[i];
                                        templink.setOnAction((ActionEvent e) -> {

                                            System.out.println("This link is clicked");
                                            //以下是以預設瀏覽器開啟，只能window喔~
                                            if (Desktop.isDesktopSupported()) {
                                                Desktop desktop = Desktop.getDesktop();
                                                try {
                                                    desktop.browse(new URI(tempurl));
                                                } catch (IOException ex) {
                                                    Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (URISyntaxException ex) {
                                                    Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                                }

                                            } else {
                                                try {
                                                    Runtime runtime = Runtime.getRuntime();
                                                    runtime.exec("xdg-open " + url);
                                                } //
                                                /*
                                Stage web = new Stage();
                                StackPane Webroot = new StackPane();
                                Scene webv = new Scene(Webroot);
                                WebView webView = new WebView();
                                WebEngine engine = new WebEngine();
                                engine = webView.getEngine();
                                engine.load(tempurl);
                                Webroot.getChildren().add(webView);
                                web.setTitle("New window");
                                web.setScene(webv);
                                web.show();*/ catch (IOException ex) {
                                                    Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }

                                        });

                                        templink.setOnMouseEntered((MouseEvent mouseevent) -> {
                                            Stage tempst = new Stage();
                                            StackPane tempWebroot = new StackPane();
                                            Scene tempweb = new Scene(tempWebroot);
                                            WebView tempwebView = new WebView();
                                            WebEngine tempengine = new WebEngine();
                                            tempengine = tempwebView.getEngine();
                                            mouseDetect.updateMouseData(mouseevent);

                                            tempst.setX(mouseDetect.getNewX() + 30);
                                            tempst.setY(mouseDetect.getNewY() + 20);
                                            tempengine.load(tempurl);
                                            tempWebroot.getChildren().add(tempwebView);
                                            tempst.setTitle("預覽");
                                            tempst.setScene(tempweb);
                                            tempst.show();
                                            templink.setOnMouseExited((MouseEvent e) -> {
                                                tempst.close();

                                            });
                                        });
                                    }
                                    box.getChildren().addAll(link);
                                    scroll.setContent(box);
                                    vbox.getChildren().addAll(scroll);

                                    // Webroot.getChildren().add(webView);
                                    WebRecom.setTitle("WebRecomend");
                                    WebRecom.setScene(WebRe);
                                    WebRecom.show();
                                    break;
                                case "Exit":
                                    System.out.println("exit");
                                    System.exit(0);
                                    break;
                                default:
                                    break;

                            }
                        }

                    }
                    );
                    mouseDetect.updateMouseData(mouseEventright);
                    contextmenu.show(mascotStage, mouseDetect.getNewX(), mouseDetect.getNewY());
                }
            }

        });
        root.getChildren().add(MascotimageView);

    }
//設定事件結束
}
