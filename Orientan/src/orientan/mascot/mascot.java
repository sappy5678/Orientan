package orientan.mascot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
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
import org.json.JSONException;
import org.json.JSONObject;
import orientan.Localelanguage;
import orientan.MascotThreadNumberManager;
import orientan.OAuth.OAuth;
import orientan.RecommendPages.RecommendData;
import orientan.RecommendPages.RecommendPageData;
import orientan.RecommendPages.RecommendPagesCrawl;
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
    private Image DefaultClimbCeilingImage;
    private ImageView MascotimageView = new ImageView(DefaultStandImage);
    //standMode Action
    private Walk walkAction;
    private Run runAction;
    private Dash dashAction;
    private Stand standAction;
    //sitMOde Action
    private SitAndDangleLegs sitAndDangleLegsAction;
    private SitAndSpinHeadAction sitAndSpinHeadAction;
    private SitWithLegsDown sitWithLegsDownAction;
    private SitWithLegsUp sitWithLegsUpAction;
    private Sit sitAction;
    //fallMode Action
    private FallingAndBouncing fallAction;
    private Drag dragAction;
    private ClimbCeiling ClimbCeilingAction;
    private ActionMode actionMode;
    private ArrayList<MascotAction> standActionList = new ArrayList<MascotAction>();
    private ArrayList<MascotAction> sitActionList = new ArrayList<MascotAction>();
    private Random random = new Random();
    private boolean isAction = false;
    private long lastTime = System.currentTimeMillis();
    private String RecommendPagesNumber = null;
    private Boolean DeafaultRecomned = false;
    private Boolean OAuthRecommedID = false;
    private Gson gson = new Gson();
    private RecommendData recommenddata = new RecommendData();
    private int PagesNumber = 0;
    private String InputRecommendID = null;

    //private double mascotdeltaX = 0.5;
    //private double mascotdeltaY = 0.02;
    //private Time currentTime;
    public mascot(loadconfig actionConfig, Mouse mouseDetect, String imgPath) {
        //初始化設定
        actionMode = new ActionMode();
        this.configList = actionConfig;
        //設定視窗初始位置
        this.imagePath = imgPath;
        this.animationManger = new TimelineManger(imagePath);
        this.DefaultStandImage = new Image(new File(imagePath + "\\shime1.png").toURI().toString());
        DefaultSitImage = new Image(new File(imagePath + "/shime11.png").toURI().toString());
        DefaultClimbCeilingImage = new Image(new File(imagePath + "/shime25.png").toURI().toString());
        mascotStage.setY(mascotenvironment.getFloor());
        mascotStage.setX(mascotenvironment.getRightWall() - 10);
        walkAction = new Walk(mascotStage, MascotimageView, configList.getData("Walk", "Move"), animationManger, imagePath);
        standAction = new Stand(mascotStage, MascotimageView, configList.getData("Stand", "Stay"), animationManger, imagePath);
        runAction = new Run(mascotStage, MascotimageView, configList.getData("Run", "Move"), animationManger, imagePath);
        dashAction = new Dash(mascotStage, MascotimageView, configList.getData("Dash", "Move"), animationManger, imagePath);
        ClimbCeilingAction = new ClimbCeiling(mascotStage, MascotimageView, configList.getData("ClimbCeiling", "Move"), animationManger, imagePath);
        fallAction = new FallingAndBouncing(mascotStage, MascotimageView, configList.getFallingData(), configList.getData("Bouncing", "Animate"), configList.getData("Jumping", "Embedded"), animationManger, isAction, imagePath, actionMode, ClimbCeilingAction);
        dragAction = new Drag(mascotStage, MascotimageView, configList.getData("Resisting", "Embedded"), animationManger, imagePath);
        standActionList.add(walkAction);
        standActionList.add(runAction);
        standActionList.add(dashAction);
        sitAndDangleLegsAction = new SitAndDangleLegs(mascotStage, MascotimageView, configList.getData("SitAndDangleLegs", "Stay"), animationManger, imagePath);
        sitAndSpinHeadAction = new SitAndSpinHeadAction(mascotStage, MascotimageView, configList.getData("SitAndSpinHeadAction", "Animate"), animationManger, imagePath);
        sitWithLegsDownAction = new SitWithLegsDown(mascotStage, MascotimageView, configList.getData("SitWithLegsDown", "Stay"), animationManger, imagePath);
        sitWithLegsUpAction = new SitWithLegsUp(mascotStage, MascotimageView, configList.getData("SitWithLegsUp", "Stay"), animationManger, imagePath);
        sitAction = new Sit(mascotStage, MascotimageView, configList.getData("Sit", "Stay"), animationManger, imagePath);
        sitActionList.add(sitAndDangleLegsAction);
        sitActionList.add(sitAndSpinHeadAction);
        sitActionList.add(sitWithLegsDownAction);
        sitActionList.add(sitWithLegsUpAction);
        //開始的random
        //actionList.get(random.nextInt(3)).play(random.nextInt(20) + 1);
        mascotStage.setY(-500);
        mascotStage.setX(random.nextInt((int) mascotenvironment.getRightWall()) + mascotenvironment.getLeftWall());
        fallAction.setNoCeiling(true);
        fallAction.Falling(0, 0);
        fallAction.setNoCeiling(false);
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
                //System.out.println(actionMode.getActionMode());

                if (actionMode.getActionMode() != 0) {
                    animationManger.StopAll();
                    if (actionMode.getActionMode() == 1) {
                        MascotimageView.setImage(DefaultStandImage);
                    } else if (actionMode.getActionMode() == 2) {
                        MascotimageView.setImage(DefaultSitImage);
                    }
                } else if (actionMode.getActionMode() == 0 && actionMode.getClimbMode() == 1) {
                    animationManger.StopAll();
                    MascotimageView.setImage(DefaultClimbCeilingImage);
                }
                //MascotimageView.setImage(DefaultStandImage);
                me.consume();
            }
        });
        scene.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                /*
                if (mascotStage.getY() == mascotenvironment.getFloor()) {
                    isAction = false;
                }
                if (!isAction) {
                    
                }*/
                if (System.currentTimeMillis() - lastTime > 1000) {
                    lastTime = System.currentTimeMillis();
                    if (actionMode.getActionMode() != 0) {
                        if (actionMode.getActionMode() == 1) {
                            standActionList.get(random.nextInt(3)).play(random.nextInt(20) + 1);
                        } else if (actionMode.getActionMode() == 2) {
                            sitActionList.get(random.nextInt(4)).play(random.nextInt(20) + 1);
                        }

                    } else if (actionMode.getActionMode() == 0 && actionMode.getClimbMode() == 1) {
                        ClimbCeilingAction.play(random.nextInt(20) + 1);

                    }
                    me.consume();
                }
            }
        });
        scene.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //isAction = true;
                if (actionMode.getClimbMode() >= 0) {
                    actionMode.setClimbMode(0);
                } else {
                    actionMode.setClimbMode(-1);
                }
                actionMode.setActionMode(0);
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
                    webcommand.setText(Localelanguage.getStringLocalelanguage("Web_recommend"));
                    CheckMenuItem noCeilingBoolean = new CheckMenuItem();
                    noCeilingBoolean.setId("No Ceiling");
                    noCeilingBoolean.setText(Localelanguage.getStringLocalelanguage("Ceiling"));
                    noCeilingBoolean.setSelected(false);

                    noCeilingBoolean.setOnAction(new EventHandler<ActionEvent>() {

                        public void handle(ActionEvent e) {
                            if (fallAction.isNoCeiling()) {
                                System.out.println("no ceiling");
                                fallAction.setNoCeiling(false);
                                noCeilingBoolean.setSelected(false);
                            } else {
                                System.out.println("have ceiling");
                                fallAction.setNoCeiling(true);
                                noCeilingBoolean.setSelected(true);
                            }
                        }
                    });
                    CheckMenuItem ClimbMode = new CheckMenuItem();
                    ClimbMode.setId("ClimbMode On/Off");
                    ClimbMode.setText(Localelanguage.getStringLocalelanguage("ClimbMode"));
                    ClimbMode.setSelected(false);
                    ClimbMode.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            System.out.println("!!");
                            System.out.println(actionMode.getClimbMode());

                            if (actionMode.getClimbMode() >= 0) {
                                System.out.println("*off");
                                actionMode.setClimbMode(-1);
                                ClimbMode.setSelected(false);
                            } else {
                                System.out.println("*on");
                                actionMode.setClimbMode(0);
                                ClimbMode.setSelected(true);
                            }
                            /*
                            switch (actionMode.getActionMode()) {
                                case -1:
                                    //System.out.println("on");
                                    actionMode.setClimbMode(0);
                                    noCeilingBoolean.setSelected(true);
                                    break;
                                default:
                                    //System.out.println("off");
                                    actionMode.setClimbMode(-1);
                                    noCeilingBoolean.setSelected(false);
                            }*/
                        }
                    });
                    MenuItem sitStandMode = new MenuItem();
                    sitStandMode.setId("sit/stand");
                    sitStandMode.setText(Localelanguage.getStringLocalelanguage("sit_stand"));
                    MenuItem delete = new MenuItem();
                    delete.setId("delete");
                    delete.setText(Localelanguage.getStringLocalelanguage("delete_key"));
                    MenuItem exit = new MenuItem();

                    exit.setId("Exit");
                    exit.setText(Localelanguage.getStringLocalelanguage("exit"));

                    ContextMenu contextmenu = new ContextMenu(webcommand, noCeilingBoolean, ClimbMode, sitStandMode, delete, exit);

                    contextmenu.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event
                        ) {

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
                                    /*
                                    //網頁推薦數量視窗
                                    if (RecommendPagesNumber == null) {
                                        final ChoiceDialog<String> choiceDialog = new ChoiceDialog("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
                                        choiceDialog.setTitle(Localelanguage.getStringLocalelanguage("recommendTitle")); //設定對話框視窗的標題列文字
                                        choiceDialog.setHeaderText(Localelanguage.getStringLocalelanguage("recommendTitleHeaderText")); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                                        choiceDialog.setContentText(Localelanguage.getStringLocalelanguage("recommendContentText")); //設定對話框的訊息文字
                                        choiceDialog.showAndWait(); //顯示對話框，並等待對話框被關閉時才繼續執行之後的程式
                                        try {
                                            RecommendPagesNumber = choiceDialog.getResult(); //可以直接用「choiceDialog.getResult()」來取代           
                                        } catch (final NoSuchElementException ex) {
                                            RecommendPagesNumber = null;
                                        }

                                        if (RecommendPagesNumber == null) {
                                            //沒有選擇生肖，而是直接關閉對話框
                                            System.out.println("您取消了選擇，期待您下次使用");
                                            break;
                                        }
                                    }*/
                                    //讀黨
                                    StringBuffer frstring = new StringBuffer();
                                    try {
                                        FileReader fr = new FileReader(System.getProperty("user.dir") + "\\UserID.json");
                                        BufferedReader br = new BufferedReader(fr);
                                        String line;
                                        while ((line = br.readLine()) != null) {
                                            frstring.append(line);
                                        }
                                    } catch (IOException e) {
                                        System.out.println(e);
                                    }
                                    //解析json格式的userid 並放進去
                                     {
                                        try {
                                            JSONObject root = new JSONObject(frstring.toString());
                                            if (!root.get("UserID").toString().equals("")) {
                                                InputRecommendID = root.get("UserID").toString();
                                            }
                                        } catch (JSONException ex) {
                                            Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    /*
                                    if (!userID.equals("")) {
                                        InputRecommendID = userID.UserID;
                                    }*/
                                    //網頁推薦id視窗
                                    if (InputRecommendID == null) {
                                        final TextInputDialog IDInputDialog = new TextInputDialog("1");
                                        IDInputDialog.setTitle(Localelanguage.getStringLocalelanguage("recommendIDTitle")); //設定對話框視窗的標題列文字
                                        IDInputDialog.setHeaderText(Localelanguage.getStringLocalelanguage("recommendIDTitleHeaderText")); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                                        IDInputDialog.setContentText(Localelanguage.getStringLocalelanguage("recommendIDContentText")); //設定對話框的訊息文字
                                        IDInputDialog.showAndWait(); //顯示對話框，並等待對話框被關閉時才繼續執行之後的程式
                                        try {
                                            InputRecommendID = IDInputDialog.getResult(); //可以直接用「choiceDialog.getResult()」來取代           
                                        } catch (final NoSuchElementException ex) {
                                            InputRecommendID = null;
                                        }

                                        if (InputRecommendID == null) {
                                            //沒有選擇生肖，而是直接關閉對話框
                                            System.out.println("您取消了選擇，期待您下次使用");
                                            break;
                                        }
                                    }
                                    Writer w = null;
                                    try {
                                        w = new FileWriter(System.getProperty("user.dir") + "\\UserID.json");
                                    } catch (IOException ex) {
                                        Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    //gson = new GsonBuilder().create();
                                    JSONObject obj = new JSONObject();

                                     {
                                        try {
                                            obj.put("UserID", InputRecommendID);
                                        } catch (JSONException ex) {
                                            Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                    //gson.toJson(obj.toString(), w);
                                     {
                                        try {
                                            w.write(obj.toString());
                                            w.close();
                                        } catch (IOException ex) {
                                            Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    if (RecommendPagesNumber == null) {
                                        //網頁推薦數量輸入視窗
                                        final TextInputDialog textInputDialog = new TextInputDialog("1");
                                        textInputDialog.setTitle(Localelanguage.getStringLocalelanguage("recommendTitle")); //設定對話框視窗的標題列文字
                                        textInputDialog.setHeaderText(Localelanguage.getStringLocalelanguage("recommendTitleHeaderText")); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                                        textInputDialog.setContentText(Localelanguage.getStringLocalelanguage("recommendContentText")); //設定對話框的訊息文字
                                        textInputDialog.showAndWait(); //顯示對話框，並等待對話框被關閉時才繼續執行之後的程式
                                        try {
                                            RecommendPagesNumber = textInputDialog.getResult(); //可以直接用「choiceDialog.getResult()」來取代           
                                        } catch (final NoSuchElementException ex) {
                                            RecommendPagesNumber = null;
                                        }

                                        if (RecommendPagesNumber == null) {

                                            System.out.println("您取消了選擇，期待您下次使用");
                                            break;
                                        }
                                    }

                                    //
                                    //處理非法字元，變成純數字
                                    StringBuffer temp = new StringBuffer();
                                    for (int k = 0; k < RecommendPagesNumber.length(); k++) {
                                        if (RecommendPagesNumber.charAt(k) == '0' || RecommendPagesNumber.charAt(k) == '1' || RecommendPagesNumber.charAt(k) == '2' || RecommendPagesNumber.charAt(k) == '3' || RecommendPagesNumber.charAt(k) == '4' || RecommendPagesNumber.charAt(k) == '5' || RecommendPagesNumber.charAt(k) == '6' || RecommendPagesNumber.charAt(k) == '7' || RecommendPagesNumber.charAt(k) == '8' || RecommendPagesNumber.charAt(k) == '9') {
                                            temp.append(RecommendPagesNumber.charAt(k));
                                        }
                                    }
                                    if (temp.toString().equals("")) {
                                        RecommendPagesNumber = "1";
                                    } else {
                                        RecommendPagesNumber = temp.toString();
                                    }
                                    System.out.println("UserID:"+ InputRecommendID);
                                    System.out.println("Number:"+ Integer.parseInt(RecommendPagesNumber));
                                    //
                                    String jsondata = "{\"descript\": \"SUCESS\", \"pages\": [{\"url\": \"https://www.facebook.com/\", \"descr\": \"<p>\\u00e8\\u00ab\\u008b\\u00e5\\u0095\\u009f\\u00e5\\u008b\\u0095\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00e7\\u009a\\u0084 JavaScript \\u00e6\\u0088\\u0096\\u00e6\\u0098\\u00af\\u00e5\\u008d\\u0087\\u00e7\\u00b4\\u009a\\u00e6\\u0088\\u0090\\u00e5\\u008f\\u00af\\u00e5\\u009f\\u00b7\\u00e8\\u00a1\\u008c JavaScript \\u00e7\\u009a\\u0084\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00ef\\u00bc\\u008c\\u00e4\\u00bb\\u00a5\\u00e4\\u00be\\u00bf\\u00e8\\u00a8\\u00bb\\u00e5\\u0086\\u008a Facebook\\u00e3\\u0080\\u0082</p>\", \"id\": 1, \"title\": \"(1) Facebook\"}, {\"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8\", \"descr\": \"\", \"id\": 2, \"title\": \"Google\"}, {\"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/DefaultPage.aspx?Menu=Default&LogExcute=Y\", \"descr\": \"\", \"id\": 3, \"title\": \"\"}, {\"url\": \"https://mail.google.com/mail/u/0/#inbox\", \"descr\": \"\\n<p>\\n<label for=\\\"Email\\\">\\n  Enter your email</label>\\n<input name=\\\"Email\\\" placeholder=\\\"Enter your email\\\" spellcheck=\\\"false\\\" type=\\\"email\\\" value=\\\"\\\">\\n<input spellcheck=\\\"false\\\" type=\\\"password\\\">\\n</input></input></p>\\n\", \"id\": 5, \"title\": \"\\u6536\\u4ef6\\u5323 (53) - sappy5678@gmail.com - Gmail\"}, {\"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?LogExcute=Y&Menu=Pot\", \"descr\": \"\", \"id\": 6, \"title\": \"\"}, {\"url\": \"https://payments.24hbuycard.com/SteamTWD\", \"descr\": \"\\n<p>1.\\u9078\\u64c7\\u5546\\u54c1\\u6578\\u91cf\\u4e26\\u5b8c\\u6574\\u586b\\u5beb\\u4ed8\\u6b3e\\u4eba\\u8cc7\\u8a0a\\u00a0\\n                    <br />2.\\u76f4\\u63a5\\u7d50\\u5e33\\u53d6\\u5f97\\u8d85\\u5546\\u4ee3\\u78bc\\u524d\\u5f80\\u8d85\\u5546\\u7e73\\u8cbb\\n                    <br />3.\\u4ed8\\u6b3e\\u5b8c\\u7562\\u5f8c\\u5c07\\u5e8f\\u865f\\u50b3\\u9001\\u624b\\u6a5f\\u7c21\\u8a0a\\u7d66\\u60a8\\n                    <br />4.\\u6709\\u4efb\\u4f55\\u554f\\u984c\\u8acb\\u806f\\u7e6b\\u00a024hbuycard \\u5ba2\\u670d \\u00a0\\n                    <br />5.\\u7dda\\u4e0a\\u5ba2\\u670d LINE ID\\uff1a@24hbuycard\\u00a0\\n                    <br />6.\\u5ba2\\u670d\\u4fe1\\u7bb1 Email\\uff1asupport@24hbuycard.com\\u00a0\\n                    <br /></br></br></br></br></br></br></p>\\n\", \"id\": 7, \"title\": \"\"}, {\"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/TMat/Materials_S.aspx?Menu=Mat\", \"descr\": \"\", \"id\": 8, \"title\": \"\"}, {\"url\": \"https://www.inside.com.tw/2015/10/13/5-games-to-learn-programming\", \"descr\": \"\\n<p>\\u770b\\u4e86\\u9019\\u9ebc\\u591a\\u9f13\\u52f5\\u4f60\\u9032\\u5165\\u7a0b\\u5f0f\\u9818\\u57df\\u7684\\u6587\\u7ae0\\uff0c\\u6709\\u6c92\\u6709\\u5f88\\u5fc3\\u52d5\\u5462\\uff1f\\u73fe\\u5728\\u7db2\\u8def\\u4e0a\\u6709\\u975e\\u5e38\\u591a\\u81ea\\u5b78\\u8cc7\\u6e90\\uff0c\\u4f8b\\u5982 Codecademy\\u3001Coursera\\u3001edX \\u7b49\\u7b49\\u7dda\\u4e0a\\u8ab2\\u7a0b\\u5e73\\u53f0\\uff0c\\u80fd\\u5920\\u8b93\\u4f60\\u514d\\u8cbb\\u4fee\\u7fd2\\u4f86\\u81ea\\u5168\\u7403\\u83c1\\u82f1\\u5927\\u5b78\\u6559\\u6388\\u89aa\\u6388\\u7684\\u7d2e\\u5be6\\u5167\\u5bb9\\u3002\\u9664\\u4e86\\u4e56\\u4e56\\u4e0a\\u8ab2\\u4e4b\\u5916\\uff0c\\u6709\\u53e6\\u5916\\u4e00\\u7a2e\\u65b9\\u6cd5\\u80fd\\u5920\\u5e36\\u4f60\\u5165\\u9580\\uff0c\\u5316\\u89e3\\u4f60\\u5c0d\\u7a0b\\u5f0f\\u7684\\u6050\\u61fc\\u2014\\u2014\\u4f86\\u73a9\\u904a\\u6232\\u7834\\u95dc\\u5427\\uff01</p>\\n<p>Jacob Gube <a href=\\\"http://sixrevisions.com/resources/games-that-teach-how-to-code/\\\" onclick=\\\"__gaTracker('send', 'event', 'outbound-article', 'http://sixrevisions.com/resources/games-that-teach-how-to-code/', '\\u63a8\\u85a6');\\\">\\u63a8\\u85a6</a> \\u4e86\\u4e94\\u7a2e\\u5bd3\\u6559\\u65bc\\u6a02\\u7684\\u7db2\\u9801\\u904a\\u6232\\uff0c\\u8b93\\u4f60\\u5728\\u73a9\\u904a\\u6232\\u7684\\u904e\\u7a0b\\u4e2d\\uff0c\\u4e5f\\u9032\\u5165\\u4e86\\u8ff7\\u4eba\\u7684\\u7a0b\\u5f0f\\u4e16\\u754c\\u3002\\u9019\\u4e9b\\u904a\\u6232\\u4e0d\\u4e00\\u5b9a\\u53ea\\u9069\\u5408\\u5c0f\\u5b69\\u73a9\\uff0c\\u6709\\u4e00\\u5169\\u500b\\u751a\\u81f3\\u6709\\u4e9b\\u8907\\u96dc\\u3002\\u5982\\u679c\\u4f60\\u662f\\u4e00\\u540d\\u60f3\\u7528\\u8f15\\u9b06\\u6709\\u8da3\\u7684\\u65b9\\u5f0f\\uff0c\\u7406\\u89e3\\u7a0b\\u5f0f\\u57fa\\u672c\\u904b\\u4f5c\\u6982\\u5ff5\\u7684\\u4eba\\uff0c\\u7576\\u7136\\u4e5f\\u80fd\\u8a66\\u8a66\\u3002</p>\\n<h3>CodeCombat</h\", \"id\": 9, \"title\": \"\\u4e94\\u500b\\u7db2\\u9801\\u904a\\u6232\\uff0c\\u8b93\\u4f60\\u9032\\u5165\\u8ff7\\u4eba\\u7684\\u7a0b\\u5f0f\\u4e16\\u754c - INSIDE \\u786c\\u585e\\u7684\\u7db2\\u8def\\u8da8\\u52e2\\u89c0\\u5bdf\"}, {\"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/UserLoan/PersonalLoan.aspx\", \"descr\": \"\\n<!DOCTYPE html PUBLIC \\\"-//W3C//DTD XHTML 1.0 Transitional//EN\\\" \\\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\\\">\\n\\n<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\n<head><title>\\r\\n\\t\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\\r\\n</title>\\n<!--head include-->\\n\\n\\n<!--head include-->\\n</head>\\n<body onload=\\\"setTimeout('TimeoutTransfer()',600000);\\\">\\n<!--\\u81ea\\u52d5\\u767b\\u51fa\\u7684\\u529f\\u80fd-->\\n\\n<!--\\u9019\\u500bifram\\u662f\\u70ba\\u4e86\\u907f\\u514ddiv\\u88abselect\\u7d66\\u906e\\u4f4f-->\\n\\n<div>\\n<table cellpadding=\\\"0\\\" cellspacing=\\\"0\\\">\\n<tr><td align=\\\"left\\\" colspan=\\\"3\\\">\\u00a0\\u7cfb\\u7d71\\u8a0a\\u606f</td></tr>\\n<tr><td>\\u00a0</td>\\n<td align=\\\"left\\\">\\r\\n    \\u60a8\\u5df2\\u8d85\\u904e\\u5341\\u5206\", \"id\": 10, \"title\": \"\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\"}, {\"url\": \"http://e-tutor.itsa.org.tw/e-Tutor/\", \"descr\": \"<p><a href=\\\"http://algorithm.csie.ncku.edu.tw/ITSAcontest/\\\" target=\\\"_blank\\\"><span>ITSA\\u00e7\\u00a8\\u008b\\u00e5\\u00bc\\u008f\\u00e6\\u00a1\\u0082\\u00e5\\u0086\\u00a0\\u00e6\\u008c\\u0091\\u00e6\\u0088\\u00b0\\u00e5\\u00a4\\u00a7\\u00e8\\u00b3\\u00bd \\u00e7\\u00b6\\u00b2\\u00e7\\u00ab\\u0099</span></a><span></span></p>\\n<p><span></span></p>\\n<p><a href=\\\"http://algorithm.csie.ncku.edu.tw/ITSA/\\\" target=\\\"_blank\\\"><span>ITSA&amp;PTC;\\u00e7\\u00b7\\u009a\\u00e4\\u00b8\\u008a\\u00e7\\u00a8\\u008b\\u00e5\\u00bc\\u008f\\u00e8\\u00a8\\u00ad\\u00e8\\u00a8\\u0088\\u00e7\\u00ab\\u00b6\\u00e8\\u00b3\\u00bd</span></a><span></span></p>\\n<p><span></span></p>\\n<p><a href=\\\"https://cpe.cse.nsysu.edu.tw/\\\" target=\\\"_blank\\\"><span>CPE\\u00e5\\u00a4\\u00a7\\u00e5\\u00ad\\u00b8\\u00e7\\u00a8\\u008b\\u00e5\\u00bc\\u008f\\u00e8\\u0083\\u00bd\\u00e5\\u008a\\u009b\\u00e6\\u00aa\\u00a2\\u00e5\\u00ae\\u009a</span></a><span></span></p>\\n<p><span></span></p>\\n\", \"id\": 12, \"title\": \"\\u7dda\\u4e0a\\u5354\\u540c\\u5b78\\u7fd2\\u5e73\\u81fa\"}, {\"url\": \"https://www.gandi.net/admin/domain\", \"descr\": \"\\n<figure>\\n<a href=\\\"/news/en/2016-12-20/10128-we_wish_you_a_merry_.xyz-mas/\\\">\\n<img alt=\\\"\\\" height=\\\"88\\\" src=\\\"https://www.gandi.net/static/images/cms/large/xyz.png\\\" width=\\\"88\\\"/>\\n</a>\\n</figure>\\n<p>As we round the corner into the last couple weeks of 2016, it's a time to think about endings and new beginnings. And what better way to do both than a new start on a new domain with the last three letters in the alphabe\\u2026\\u00a0 <a href=\\\"/news/en/2016-12-20/10128-we_wish_you_a_merry_.xyz-mas/\\\">Read more</a></p> \", \"id\": 13, \"title\": \"\\u57df\\u540d - \\u7ba1\\u7406\\u4ecb\\u9762 - Gandi.net\"}, {\"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/Search/SearchNew.aspx\", \"descr\": \"\\n<!DOCTYPE html PUBLIC \\\"-//W3C//DTD XHTML 1.0 Transitional//EN\\\" \\\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\\\">\\n\\n<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\n<head><title>\\r\\n\\t\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\\r\\n</title>\\n<!--head include-->\\n\\n\\n<!--head include-->\\n\\n</head>\\n<body onload=\\\"javascript:InnerImage('mSearch1','');setTimeout('TimeoutTransfer()',1*60*1000);\\\">\\n<!--\\u81ea\\u52d5\\u767b\\u51fa\\u7684\\u529f\\u80fd-->\\n\\n<!--\\u9019\\u500bifram\\u662f\\u70ba\\u4e86\\u907f\\u514ddiv\\u88abselect\\u7d66\\u906e\\u4f4f-->\\n\\n<div>\\n<table cellpadding=\\\"0\\\" cellspacing=\\\"0\\\">\\n<tr><td align=\\\"left\\\" colspan=\\\"3\\\">\\u00a0\\u7cfb\\u7d71\\u8a0a\\u606f</td></tr>\\n<tr\", \"id\": 14, \"title\": \"\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\"}, {\"url\": \"https://github.com/sappy5678/Orientan\", \"descr\": \"\\n<p>\\n<span itemprop=\\\"about\\\">\\n          simple desktop pet (desktop mascot) use java to create\\n        </span>\\n</p>\\n\", \"id\": 15, \"title\": \"sappy5678/Orientan: simple desktop pet (desktop mascot) use java to create\"}, {\"url\": \"http://skiesblog.blogspot.tw/2010/01/threading-in-python.html\", \"descr\": \"<!DOCTYPE html>\\n\\n<html dir=\\\"ltr\\\">\\n<head>\\n<meta content=\\\"width=1100\\\" name=\\\"viewport\\\"/>\\n<meta content=\\\"text/html; charset=utf-8\\\" http-equiv=\\\"Content-Type\\\"/>\\n<meta content=\\\"blogger\\\" name=\\\"generator\\\"/>\\n\\n\\n\\n\\n\\n\\n<!--[if IE]><script type=\\\"text/javascript\\\" src=\\\"https://www.blogger.com/static/v1/jsbin/3032875878-ieretrofit.js\\\"></script>\\n<![endif]-->\\n\\n<meta content=\\\"http://skiesblog.blogspot.com/2010/01/threading-in-python.html\\\" property=\\\"og:url\\\"/>\\n<meta content=\\\"Threading in Python\\\" property=\\\"og:title\\\"/>\\n<\", \"id\": 16, \"title\": \"\\u6df1\\u85cd\\u6dfa\\u85cd: Threading in Python\"}, {\"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/Search/SearchResult.aspx\", \"descr\": \"\", \"id\": 18, \"title\": \"\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\"}, {\"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"descr\": \"\", \"id\": 19, \"title\": \"\"}, {\"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/IFrameSub.aspx?SysURL=https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"descr\": \"\", \"id\": 20, \"title\": \"\"}, {\"url\": \"https://www.cloudflare.com/a/overview/sappy5678.com.tw\", \"descr\": \"\", \"id\": 21, \"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\"}, {\"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?Menu=New\", \"descr\": \"\", \"id\": 22, \"title\": \"portalx.yzu.edu.tw\"}, {\"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/UserLoan/PersonalLoan.aspx?page=1\", \"descr\": \"\\n<!DOCTYPE html PUBLIC \\\"-//W3C//DTD XHTML 1.0 Transitional//EN\\\" \\\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\\\">\\n\\n<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\n<head><title>\\r\\n\\t\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\\r\\n</title>\\n<!--head include-->\\n\\n\\n<!--head include-->\\n</head>\\n<body onload=\\\"setTimeout('TimeoutTransfer()',600000);\\\">\\n<!--\\u81ea\\u52d5\\u767b\\u51fa\\u7684\\u529f\\u80fd-->\\n\\n<!--\\u9019\\u500bifram\\u662f\\u70ba\\u4e86\\u907f\\u514ddiv\\u88abselect\\u7d66\\u906e\\u4f4f-->\\n\\n<div>\\n<table cellpadding=\\\"0\\\" cellspacing=\\\"0\\\">\\n<tr><td align=\\\"left\\\" colspan=\\\"3\\\">\\u00a0\\u7cfb\\u7d71\\u8a0a\\u606f</td></tr>\\n<tr><td>\\u00a0</td>\\n<td align=\\\"left\\\">\\r\\n    \\u60a8\\u5df2\\u8d85\\u904e\\u5341\\u5206\", \"id\": 23, \"title\": \"\"}, {\"url\": \"https://mail.google.com/mail/u/0/#label/%E5%B7%A5%E4%BD%9C\", \"descr\": \"\\n<p>\\n<label for=\\\"Email\\\">\\n  Enter your email</label>\\n<input name=\\\"Email\\\" placeholder=\\\"Enter your email\\\" spellcheck=\\\"false\\\" type=\\\"email\\\" value=\\\"\\\">\\n<input spellcheck=\\\"false\\\" type=\\\"password\\\">\\n</input></input></p>\\n\", \"id\": 24, \"title\": \"\\\"\\u5de5\\u4f5c\\\" - sappy5678@gmail.com - Gmail\"}, {\"url\": \"https://css.itsa.org.tw/\", \"descr\": \"<p align=\\\"left\\\"><span><strong>\\u5275\\u4f5c\\u793e\\u7fa4\\u670d\\u52d9\\u5e73\\u81fa\\u529f\\u80fd\\u7c21\\u4ecb</strong></span></p>\\n<p align=\\\"left\\\"><span><strong>\\u5c08\\u6848\\u5e73\\u81fa\\uff1a</strong></span></p>\\n<p align=\\\"left\\\"><span>\\u5c08\\u6848\\u5e73\\u81fa\\u9644\\u5c6c\\u65bc\\u5275\\u4f5c\\u793e\\u7fa4\\u670d\\u52d9\\u5e73\\u81fa\\uff0c\\u8b93\\u8edf\\u9ad4\\u611b\\u597d\\u8005\\u53ef\\u4ee5\\u5171\\u540c\\u53c3\\u8207\\u5c08\\u6848\\u958b\\u767c\\u7684\\u5e73\\u81fa\\uff0c\\u958b\\u767c\\u8005\\u53ef\\u5229\\u7528\\u6b64\\u5e73\\u81fa\\u767c\\u5c55\\u8207\\u5206\\u4eab\\u5176\\u5c08\\u6848\\u958b\\u767c\\uff0c\\u4f7f\\u7528\\u8005\\u4e5f\\u53ef\\u5728\\u6b64\\u53d6\\u5f97\\u8edf\\u9ad4\\u4e26\\u63d0\\u4f9b\\u610f\\u898b\\uff0c\\u8de8\\u6821\\u8cc7\\u6e90\\u4e2d\\u5fc3\\u6240\\u9032\\u884c\\u958b\\u767c\\u7684\\u5c08\\u6848\\uff0c\\u4e5f\\u53ef\\u4ee5\\u5728\\u6b64\\u5e73\\u81fa\\u4e0a\\u9032\\u884c\\u958b\\u767c\\uff0c\\u52a0\\u4ee5\\u8207\\u5176\\u4ed6\\u5927\\u5c08\\u9662\\u6821\\u76f8\\u4e92\\u4ea4\\u6d41\\u3002\\r\\n</span></p>\\n<!--\\r\\n<p align=\\\"left\\\"><span><strong>\\u8cc7\\u8a0a\\u5206\\u4eab:</strong></span></p>\\r\\n<p align=\\\"left\\\"><span>\\u4f7f\\u7528\\u8005\\u53ef\\u4ee5\\u5728\\u5275\\u4f5c\\u793e\\u7fa4\\u670d\\u52d9\\u5e73\\u81fa\\u4e0a\\u9ede\\u9078\\u300c\\u8cc7\\u8a0a\\u5206\\u4eab\\u300d\\u4e0b\\uff0c\\u627e\\u5230\\u4efb\\u4f55\\u8207\\u81ea\\u7531\\u8edf\\u9ad4\\u76f8\\u95dc\\u7684\\u8cc7\\u8a0a\\u4ecb\\u7d39\\uff0c\\u4e5f\\u53ef\\u4ee5\\u5c07\\u4f7f\\u7528\\u81ea\\u7531\\u8edf\\u9ad4\\u7684\\u5bf6\\u8cb4\\u5fc3\\u5f97\\u5206\\u4eab\\u81f3\\u5e73\\u81fa\\u4e0a\\u3002</span></p>\\r\\n-->\\n<p align=\\\"left\\\"><span><strong>\\u7fa4\\u7d44\\uff1a</strong></span></p>\\n<p align=\\\"\", \"id\": 25, \"title\": \"\\u5275\\u4f5c\\u793e\\u7fa4\\u670d\\u52d9\\u5e73\\u81fa\"}, {\"url\": \"https://github.com/sappy5678/Orientan/pulls\", \"descr\": \"\", \"id\": 26, \"title\": \"Pull Requests \\u00b7 sappy5678/Orientan\"}, {\"url\": \"https://lrs.itsa.org.tw/\", \"descr\": \"\\n\\n<p><p></p><p></p><span><span><p>\\u5b78\\u7fd2\\u8cc7\\u6e90\\u670d\\u52d9\\u5e73\\u81fa\\u6210\\u7acb\\u7684\\u76ee\\u7684\\u5728\\u65bc\\u8490\\u96c6\\u5404\\u5927\\u5c08\\u9662\\u6821\\u8207\\u570b\\u5167\\u5916\\u7684\\u958b\\u653e\\u5f0f\\u8ab2\\u7a0b\\u548c\\u8edf\\u9ad4\\u8cc7\\u6e90\\uff0c</p><p>\\u4e26\\u63d0\\u4f9b\\u4fbf\\u6377\\u7684\\u4f7f\\u7528\\u4ecb\\u9762\\uff0c\\u4e0d\\u50c5\\u8b93\\u6559\\u5e2b\\u53ef\\u4ee5\\u5feb\\u901f\\u5206\\u4eab\\u6559\\u6750\\uff0c\\u540c\\u6642\\u5b78\\u751f\\u4e5f\\u53ef\\u4ee5\\u6709\\u6548\\u7684\\u904b\\u7528\\u8207\\u5b78\\u7fd2\\u3002</p></span></span><span><p></p><span><br /></span></span><span><span><p>\\u7db2\\u7ad9\\u4e5f\\u6703\\u540c\\u6b65\\u8a18\\u9304\\u5b78\\u7fd2\\u8cc7\\u6e90\\u4e0b\\u8f09\\u6b21\\u6578\\u8207\\u4f7f\\u7528\\u60c5\\u6cc1\\uff0c\\u4ee5\\u9632\\u4e0d\\u7576\\u4f7f\\u7528\\uff0c</p><p>\\u540c\\u6642\\u4e5f\\u4f5c\\u70ba\\u7db2\\u7ad9\\u6d41\\u91cf\\u4e4b\\u5206\\u6790\\u4f7f\\u7528\\uff0c\\u4ee5\\u5229\\u5e73\\u81fa\\u4e4b\\u7814\\u7a76\\u4eba\\u54e1\\u80fd\\u66f4\\u52a0\\u4e86\\u89e3\\u5e73\\u81fa\\u73fe\\u6cc1\\u3002</p></span></span><p></p><a href=\\\"#\\\"></a><p></p></p>\\n\", \"id\": 27, \"title\": \"\\u5b78\\u7fd2\\u8cc7\\u6e90\\u670d\\u52d9\\u5e73\\u81fa\"}, {\"url\": \"https://screeps.com/\", \"descr\": \"\\n<h4>True strategy game</h4>\\n<p>Units, base, mining resources, territory control, economy, manufacturing, transporting, logistics, reconnaissance \\u00e2\\u0080\\u0093 all the attributes of a real strategy game <strong>which you will have to\\u00e2\\u0080\\u00a6 program</strong>!</p>\\n\", \"id\": 28, \"title\": \"Screeps - MMO strategy sandbox game for programmers\"}, {\"url\": \"https://github.com/PyMySQL/PyMySQL\", \"descr\": \"<a href=\\\"http://pymysql.readthedocs.io/en/latest/?badge=latest\\\"><img alt=\\\"Documentation Status\\\" data-canonical-src=\\\"https://readthedocs.org/projects/pymysql/badge/?version=latest\\\" src=\\\"https://camo.githubusercontent.com/64a24fd99c7c3155fb9850183c448529e2662637/68747470733a2f2f72656164746865646f63732e6f72672f70726f6a656374732f70796d7973716c2f62616467652f3f76657273696f6e3d6c6174657374\\\"/></a>\\n<a href=\\\"https://travis-ci.org/PyMySQL/PyMySQL\\\"><img alt=\\\"https://travis-ci.org/PyMySQL/PyMySQL.svg?branch=\", \"id\": 29, \"title\": \"PyMySQL/PyMySQL: Pure Python MySQL Client\"}, {\"url\": \"https://www.cloudflare.com/a/dns/sappy5678.com.tw\", \"descr\": \"\", \"id\": 33, \"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\"}, {\"url\": \"https://www.youtube.com/watch?v=pKi0Ojyg3Sg&list=PLO1vcxMs-q4m4VyVFWFmK9thGx-Hxe4pH&index=1\", \"descr\": \"\\n<p>\\n<span title=\\\"\\u6b63\\u5728\\u8f09\\u5165\\u5716\\u793a\\\"></span>\\n<span>\\n        \\u8655\\u7406\\u4e2d...\\n    </span>\\n</p>\\n\", \"id\": 34, \"title\": \"\\u6771\\u65b9\\u304b\\u308f\\u3044\\u3044\\u66f2\\u30e1\\u30c9\\u30ec\\u30fc - YouTube\"}, {\"url\": \"http://studyhost.blogspot.tw/2016/12/aspnet-web-1.html\", \"descr\": \"\\n<p><img height=\\\"402\\\" src=\\\"http://arock.blob.core.windows.net/blogdata201612/22-124303-d8b2c4de-9a8c-48da-83f1-7c0d36de3ab6.png\\\" width=\\\"640\\\"><br />\\u8b1b\\u6280\\u8853\\u4e4b\\u524d\\uff0c\\u5148\\u8b93\\u6211\\u8aaa\\u8aaa\\u6700\\u8fd1\\u767c\\u751f\\u7684\\u5e7e\\u4ef6\\u4e8b\\u60c5\\u2026</br></img></p> <p><u><strong>\\u9019\\u6642\\u4ee3\\u6c92\\u6709\\u4ec0\\u9ebc\\u8da8\\u52e2\\u597d\\u8aaa\\u7684\\u2026<br /></strong></u>\\u524d\\u4e9b\\u65e5\\u5b50\\uff0c\\u6211\\u5728\\u67d0\\u500b\\u7814\\u8a0e\\u6703\\u7576\\u4e2d\\uff0c\\u78b0\\u5230\\u4e00\\u4f4d\\u5b78\\u54e1\\uff0c\\u4ed6\\u5728\\u4e0a\\u8ab2\\u524d\\u627e\\u5230\\u4e86\\u6211\\uff0c\\u6436\\u5148\\u554f\\u6211\\u5148\\u524d\\u63d0\\u904e\\u8981\\u958b\\u7684\\u67b6\\u69cb\\u8a2d\\u8a08\\u8ab2\\u7a0b\\uff0c\\u6700\\u8fd1\\u4e00\\u6b21\\u4f55\\u6642\\u6703\\u958b\\u5462? \\u6211\\u5f88\\u9ad8\\u8208\\u4ed6\\u5c0d\\u9019\\u500b\\u8ab2\\u7a0b\\u6709\\u8208\\u8da3\\uff0c\\u4f46\\u662f\\u537b\\u53ea\\u80fd\\u9eef\\u7136\\u5730\\u8ddf\\u4ed6\\u8aaa\\uff0c\\u4eca\\u5e74\\u5be6\\u5728\\u4e0d\\u4e00\\u5b9a\\u6709\\u7a7a\\u4e86\\u3002</p><p>\\u9664\\u4e86\\u6642\\u9593\\u7684\\u56e0\\u7d20\\u4e4b\\u5916\\uff0c\\u5176\\u5be6\\u4e5f\\u56e0\\u70ba\\u6709\\u53e6\\u4e00\\u500b\\u9867\\u616e\\uff0c\\u5c31\\u662f\\u9019\\u5e7e\\u5e74\\u7684\\u8cc7\\u8a0a\\u79d1\\u6280\\u8b8a\\u5316\\u5be6\\u5728\\u4e0d\\u5c0f\\uff0c\\u5728\\u983b\\u7e41\\u7684\\u8b8a\\u5316\\u7576\\u4e2d\\uff0c\\u8981\\u5efa\\u69cb\\u51fa\\u76f8\\u5c0d\\u7a69\\u5b9a\\u7684\\u67b6\\u69cb\\u78ba\\u5be6\\u4e0d\\u6613\\uff0c\\u4e5f\\u56e0\\u70ba\\u6709\\u611f\\u65bc\\u6b64\\uff0c\\u5728\\u9019\\u5834\\u7814\\u8a0e\\u6703\\u4e2d\\uff0c\\u6211\\u5f88\\u7336\\u8c6b\\u7684\\u8981\\u4e0d\\u8981\\u63d0\\u66f4\\u591a\\u6211\\u5c0d\\u65bc\\u9019\\u5e7e\\u5e74\\u6280\\u8853\\u8b8a\\u5316\\u81ea\\u5df1\\u7684\\u770b\\u6cd5\\u3002</p> <blockquote> <p align=\\\"left\\\"></p></blockquote> <p><st\", \"id\": 35, \"title\": \".NET Walker: asp.net Web\\u958b\\u767c\\u6846\\u67b6 (1) - \\u5929\\u4e0b\\u6b66\\u529f\\uff0c\\u552f\\u5feb\\u4e0d\\u7834\"}, {\"url\": \"http://cychss6305.pixnet.net/blog/post/170579558-e-tutor%E6%80%8E%E9%BA%BC%E4%BD%BF%E7%94%A8\", \"descr\": \"\\n<p><span>\\u00e4\\u00bb\\u008a\\u00e5\\u00a4\\u00a9\\u00e5\\u00b0\\u00b1\\u00e4\\u00be\\u0086\\u00e6\\u0095\\u0099\\u00e6\\u0095\\u0099\\u00e5\\u0090\\u0084\\u00e4\\u00bd\\u008d\\u00e6\\u0080\\u008e\\u00e9\\u00ba\\u00bc\\u00e4\\u00bd\\u00bf\\u00e7\\u0094\\u00a8\\u00e4\\u00bb\\u00a5\\u00e4\\u00b8\\u008b\\u00e9\\u0080\\u0099\\u00e5\\u0080\\u008b\\u00e7\\u00b7\\u00b4\\u00e7\\u00bf\\u0092\\u00e5\\u00af\\u00ab\\u00e7\\u00a8\\u008b\\u00e5\\u00bc\\u008f\\u00e7\\u009a\\u0084\\u00e9\\u00a1\\u008c\\u00e5\\u00ba\\u00ab\\u00e7\\u00b6\\u00b2\\u00e7\\u00ab\\u0099</span></p>\\n<p><span><span>E-tutor</span></span> \\u00a0\\u00a0<span><a href=\\\"http://e-tutor.itsa.org.tw/e-Tutor/login/index.php\\\">http://e-tutor.itsa.org.tw/e-Tutor/login/index.php</a></span></p>\\n<p><span>\\u00e9\\u00a6\\u0096\\u00e5\\u0085\\u0088\\u00ef\\u00bc\\u008c\\u00e9\\u00bb\\u009e\\u00e5\\u0085\\u00a5\\u00e4\\u00bb\\u00a5\\u00e4\\u00b8\\u008a\\u00e9\\u0080\\u00a3\\u00e7\\u00b5\\u0090\\u00ef\\u00bc\\u008c\\u00e5\\u0086\\u008d\\u00e5\\u0089\\u00b5\\u00e5\\u0080\\u008b\\u00e5\\u00b8\\u00b3\\u00e8\\u0099\\u009f</span></p>\\n<p><img alt=\\\"\\\" height=\\\"346\\\" src=\\\"https://pic.pimg.tw/cychss6305/1470143950-1952797486_n.png?v=1470143952\\\" title=\\\"\\\" width=\\\"710\\\"/></p>\\n<p><span>\\u00e5\\u0086\\u008d\\u00e4\\u00be\\u0086\\u00ef\\u00bc\\u008c\\u00e5\\u00a1\\u00ab\\u00e5\\u00a5\\u00bd\\u00e5\\u0090\\u0084\\u00e9\\u00a0\", \"id\": 36, \"title\": \"E-tutor\\u600e\\u9ebc\\u4f7f\\u7528 @ cychss6305\\u7684\\u90e8\\u843d\\u683c :: \\u75de\\u5ba2\\u90a6 PIXNET ::\"}, {\"url\": \"https://graph.qq.com/oauth/show?which=Login&display=pc&response_type=code&client_id=100270989&redirect_uri=https%3A%2F%2Fpassport.csdn.net%2Faccount%2Flogin%3Foauth_provider%3DQQProvider&state=test\", \"descr\": \"\", \"id\": 41, \"title\": \"QQ\\u5e10\\u53f7\\u5b89\\u5168\\u767b\\u5f55\"}, {\"url\": \"https://puremonkey2010.blogspot.tw/2012/05/python-python.html\", \"descr\": \"<!DOCTYPE html>\\n\\n<html dir=\\\"ltr\\\" xmlns=\\\"http://www.w3.org/1999/xhtml\\\" xmlns:b=\\\"http://www.google.com/2005/gml/b\\\" xmlns:data=\\\"http://www.google.com/2005/gml/data\\\" xmlns:expr=\\\"http://www.google.com/2005/gml/expr\\\">\\n</html><p><span><span><b>Thread Objects :</b></span></span><span>\\u00a0</span><br /><span>\\u5728 Python \\u53ef\\u4ee5\\u4f7f\\u7528\\u6a21\\u7d44\\u00a0</span><a href=\\\"http://docs.python.org/library/threading.html\\\" rel=\\\"nofollow\\\" target=\\\"_new\\\"><b>threading</b></a><span>\\u00a0\\u4f86\\u9032\\u884c\\u591a\\u7dda\\u7a0b\\u7684\\u8a2d\\u8a08, \\u5728\\u8a72\\u6a21\\u7d44\\u4e2d\\u6709\\u500b\\u985e\\u5225\\u00a0</span><a href=\\\"http://docs.python.org/library/\", \"id\": 43, \"title\": \"\\u7a0b\\u5f0f\\u624e\\u8a18: [ Python \\u5e38\\u898b\\u554f\\u984c ] Python \\u7dda\\u7a0b\\u4f7f\\u7528\\u4ecb\\u7d39\"}, {\"url\": \"http://yanghao.org/tools/readability\", \"descr\": \"\\n\\n<p>\\n<form method=\\\"GET\\\">\\n        \\u8bf7\\u8f93\\u5165url:\\n        <input autofocus=\\\"\\\" name=\\\"url\\\" size=\\\"70\\\" type=\\\"text\\\" value=\\\"http://\\\"/>\\n<button type=\\\"submit\\\">\\u83b7\\u53d6\\u7f51\\u9875\\u5185\\u5bb9</button>\\n</form>\\n</p>\\n\\n\\n\\n\", \"id\": 44, \"title\": \"readability demo\"}, {\"url\": \"http://mike-web-learn.blogspot.tw/2016/06/python-threading.html\", \"descr\": \"<!DOCTYPE html>\\n\\n<html dir=\\\"ltr\\\">\\n<head>\\n<meta content=\\\"width=1100\\\" name=\\\"viewport\\\"/>\\n<meta content=\\\"text/html; charset=utf-8\\\" http-equiv=\\\"Content-Type\\\"/>\\n<meta content=\\\"blogger\\\" name=\\\"generator\\\"/>\\n\\n\\n\\n\\n\\n\\n<!--[if IE]><script type=\\\"text/javascript\\\" src=\\\"https://www.blogger.com/static/v1/jsbin/3032875878-ieretrofit.js\\\"></script>\\n<![endif]-->\\n<meta content=\\\"http://mike-web-learn.blogspot.com/2016/06/python-threading.html\\\" property=\\\"og:url\\\"/>\\n<meta content=\\\"[Python] threading \\u7bc4\\u4f8b\\\" property=\\\"og:title\\\"/\", \"id\": 45, \"title\": \"Mike \\u541b\\u7684\\u7db2\\u8def\\u5b78\\u7fd2: [Python] threading \\u7bc4\\u4f8b\"}, {\"url\": \"http://lolikitty.pixnet.net/blog/post/127262181-python-3-%E5%85%A5%E9%96%80%EF%BC%9A%E5%9F%B7%E8%A1%8C%E7%B7%92-(%E7%B7%9A%E7%A8%8B-thread)\", \"descr\": \"\\n<p>\\u00e7\\u0084\\u00a1\\u00e5\\u00bb\\u00ba\\u00e6\\u00a7\\u008b\\u00e5\\u00ad\\u0090\\u00e7\\u0089\\u0088\\u00e6\\u009c\\u00ac\\u00ef\\u00bc\\u009a</p>\\n<pre>import threading, time\\r\\n\\r\\nclass MyClass (threading.Thread): # \\u00e7\\u00b9\\u00bc\\u00e6\\u0089\\u00bf Thread \\u00e9\\u00a1\\u009e\\u00e5\\u0088\\u00a5\\r\\n       def run(self): # \\u00e8\\u00a6\\u0086\\u00e8\\u00bc\\u0089 (Override) Thread \\u00e9\\u00a1\\u009e\\u00e5\\u0088\\u00a5\\u00e7\\u009a\\u0084\\u00e6\\u0096\\u00b9\\u00e6\\u00b3\\u0095(\\u00e5\\u0087\\u00bd\\u00e6\\u0095\\u00b8)\\r\\n              for i in range(5): # \\u00e8\\u00bf\\u00b4\\u00e5\\u009c\\u0088\\u00e5\\u009f\\u00b7\\u00e8\\u00a1\\u008c\\u00e4\\u00ba\\u0094\\u00e6\\u00ac\\u00a1\\r\\n                     print('ok') # \\u00e8\\u00bc\\u00b8\\u00e5\\u0087\\u00ba ok\\r\\n                     time.sleep(1) # \\u00e6\\u009a\\u00ab\\u00e5\\u0081\\u009c\\u00e4\\u00b8\\u0080\\u00e7\\u00a7\\u0092\\u00ef\\u00bc\\u008c\\u00e5\\u00a6\\u0082\\u00e6\\u009e\\u009c\\u00e8\\u00a6\\u0081\\u00e6\\u009a\\u00ab\\u00e5\\u0081\\u009c 0.1\\u00e7\\u00a7\\u0092\\u00e5\\u008f\\u00af\\u00e5\\u00af\\u00ab\\u00e6\\u0088\\u0090 time.seep(0.1)\\r\\n\\r\\nMyClass().start() # \\u00e5\\u0095\\u009f\\u00e5\\u008b\\u0095\\u00e5\\u009f\\u00b7\\u00e8\\u00a1\\u008c\\u00e7\\u00b7\\u0092\\r\\n</pre>\\n<p>\\u00a0</p>\\n<p>\\u00a0\\u00a0\\u00a0\\u00a0\\u00a0\\u00a0\\u00a0\\u00a0\\u00e5\\u00a6\\u0082\\u00e6\\u009e\\u009c\\u00e8\\u00a6\\u0081\\u00e5\\u009c\\u00a8\\u00e6\\u0093\\u0081\\u00e6\\u009c\\u0089\\u00e5\\u009f\\u00b7\\u00e8\\u00a1\\u008c\\u00e7\\u00b7\\u0092\\u00e7\\u0089\\u00a9\\u00e4\\u00bb\\u00b6\\u00e5\\u0088\\u009d\\u00e5\\u00a7\\u008b\\u00e5\", \"id\": 46, \"title\": \"Python 3 \\u5165\\u9580\\uff1a\\u57f7\\u884c\\u7dd2 (\\u7dda\\u7a0b/Thread) @ \\u5f65\\u9716 \\u5be6\\u9a57\\u7b46\\u8a18 :: \\u75de\\u5ba2\\u90a6 PIXNET ::\"}, {\"url\": \"https://docs.python.org/3/library/configparser.html\", \"descr\": \"\\n<span></span>\\n<p><strong>Source code:</strong> <a href=\\\"https://hg.python.org/cpython/file/3.6/Lib/configparser.py\\\">Lib/configparser.py</a></p>\\n<hr/>\\n<p>This module provides the <a href=\\\"#configparser.ConfigParser\\\" title=\\\"configparser.ConfigParser\\\"><code><span>ConfigParser</span></code></a> class which implements a basic\\nconfiguration language which provides a structure similar to what\\u2019s found in\\nMicrosoft Windows INI files.  You can use this to write Python programs which\\ncan be customized by \", \"id\": 47, \"title\": \"14.2. configparser \\u2014 Configuration file parser \\u2014 Python 3.6.0 documentation\"}, {\"url\": \"http://www.24hbuycard.com/\", \"descr\": \"\", \"id\": 48, \"title\": \"24\\u5c0f\\u6642\\u8cb7\\u5361\\u301024hbuycard\\u3011\\u8d85\\u5546\\u7e73\\u8cbb\\u81ea\\u52d5\\u767c\\u5361-steam google apple dmm \\u9322\\u5305\\u4ee3\\u78bc\\u5132\\u503c\\u79ae\\u7269\\u5361\\u514c\\u63db/\\u65e5\\u5e63\\u7f8e\\u5143\\u53f0\\u5e63\"}, {\"url\": \"https://puremonkey2010.blogspot.tw/2012/07/python-python-threading.html\", \"descr\": \"<!DOCTYPE html>\\n\\n<html dir=\\\"ltr\\\" xmlns=\\\"http://www.w3.org/1999/xhtml\\\" xmlns:b=\\\"http://www.google.com/2005/gml/b\\\" xmlns:data=\\\"http://www.google.com/2005/gml/data\\\" xmlns:expr=\\\"http://www.google.com/2005/gml/expr\\\">\\n</html><p><span><span><b>Thread \\u985e\\u5225\\u7684\\u4f7f\\u7528 :</b></span></span><span>\\u00a0</span><br /><span>\\u518d\\u770b\\u770b\\u53e6\\u5916\\u4e00\\u7a2e\\u5275\\u5efa\\u7dda\\u7a0b\\u7684\\u65b9\\u6cd5 :\\u00a0</span><br /><div>\\n\\n<ol start=\\\"1\\\">\\n<li><span><span>import</span><span>\\u00a0\\u00a0threading, time, random\\u00a0\\u00a0\\u00a0\\u00a0</span></span></li>\\n<li><span>count =\\u00a0\\u00a0<span>0</span><span>\\u00a0\\u00a0\\u00a0\\u00a0</span></span></li>\\n<li><s\", \"id\": 49, \"title\": \"\\u7a0b\\u5f0f\\u624e\\u8a18: [ Python \\u6587\\u7ae0\\u6536\\u96c6 ] Python\\u6a21\\u584a\\u5b78\\u7fd2 - threading \\u591a\\u7dda\\u7a0b\\u63a7\\u5236\\u548c\\u8655\\u7406\"}, {\"url\": \"http://williewu.blogspot.tw/2007/10/python-configparser.html\", \"descr\": \"\\n<p><p xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\u7576\\u6211\\u5011\\u5728\\u7528 python \\u5beb\\u7a0b\\u5f0f\\u7684\\u6642\\u5019\\uff0c\\u6709\\u6642\\u9700\\u8981\\u5c07\\u4e00\\u4e9b\\u8a2d\\u5b9a\\u7368\\u7acb\\u51fa\\u4f86\\u6210\\u4e00\\u500b\\u8a2d\\u5b9a\\u6a94\\uff0c\\u65b9\\u4fbf\\u4f7f\\u7528\\u8005\\u4f86\\u4fee\\u6539\\u8a2d\\u5b9a\\uff0c\\u6b64\\u6642\\u6211\\u5011\\u53ef\\u4ee5\\u4f7f\\u7528 ConfigParser \\u6a21\\u7d44\\u4f86\\u5e6b\\u6211\\u5011\\u9054\\u5230\\u6b64\\u4e00\\u76ee\\u7684\\u3002</p></p><p><span>\\u4f7f\\u7528\\u7684\\u65b9\\u6cd5\\u5f88\\u7c21\\u55ae\\uff0c\\u5047\\u8a2d\\u9019\\u908a\\u6709\\u4e00\\u500b test.conf \\u7684\\u8a2d\\u5b9a\\u6a94\\uff0c\\u5167\\u5bb9\\u5982\\u4e0b\\uff1a<br /><blockquote>[core]<br />cmd = /usr/bin/vim</blockquote></span></p><p>[mod]<br />safe_edit = yes<br />time_interval = 20</p>\", \"id\": 50, \"title\": \"Willie's Blog: \\u4f7f\\u7528 python \\u7684 ConfigParser \\u6a21\\u7d44\"}, {\"url\": \"http://python.jobbole.com/81546/\", \"descr\": \"\\n<p><a href=\\\"http://python.jobbole.com/81544/\\\" target=\\\"_blank\\\">\\u4e0a\\u4e00\\u7bc7</a>\\u00a0\\u4ecb\\u7ecd\\u4e86thread\\u6a21\\u5757\\uff0c\\u4eca\\u5929\\u6765\\u5b66\\u4e60Python\\u4e2d\\u53e6\\u4e00\\u4e2a\\u64cd\\u4f5c\\u7ebf\\u7a0b\\u7684\\u6a21\\u5757\\uff1athreading\\u3002threading\\u901a\\u8fc7\\u5bf9thread\\u6a21\\u5757\\u8fdb\\u884c\\u4e8c\\u6b21\\u5c01\\u88c5\\uff0c\\u63d0\\u4f9b\\u4e86\\u66f4\\u65b9\\u4fbf\\u7684API\\u6765\\u64cd\\u4f5c\\u7ebf\\u7a0b\\u3002\\u4eca\\u5929\\u5185\\u5bb9\\u6bd4\\u8f83\\u591a\\uff0c\\u95f2\\u8bdd\\u5c11\\u8bf4\\uff0c\\u73b0\\u5728\\u5c31\\u5f00\\u59cb\\u5207\\u5165\\u6b63\\u9898\\uff01</p>\\n<h2><a name=\\\"t0\\\"></a>threading.Thread</h2>\\n<p>Thread \\u662fthreading\\u6a21\\u5757\\u4e2d\\u6700\\u91cd\\u8981\\u7684\\u7c7b\\u4e4b\\u4e00\\uff0c\\u53ef\\u4ee5\\u4f7f\\u7528\\u5b83\\u6765\\u521b\\u5efa\\u7ebf\\u7a0b\\u3002\\u6709\\u4e24\\u79cd\\u65b9\\u5f0f\\u6765\\u521b\\u5efa\\u7ebf\\u7a0b\\uff1a\\u4e00\\u79cd\\u662f\\u901a\\u8fc7\\u7ee7\\u627fThread\\u7c7b\\uff0c\\u91cd\\u5199\\u5b83\\u7684run\\u65b9\\u6cd5\\uff1b\\u53e6\\u4e00\\u79cd\\u662f\\u521b\\u5efa\\u4e00\\u4e2athreading.Thread\\u5bf9\\u8c61\\uff0c\\u5728\\u5b83\\u7684\\u521d\\u59cb\\u5316\\u51fd\\u6570\\uff08__init__\\uff09\\u4e2d\\u5c06\\u53ef\\u8c03\\u7528\\u5bf9\\u8c61\\u4f5c\\u4e3a\\u53c2\\u6570\\u4f20\\u5165\\u3002\\u4e0b\\u9762\\u5206\\u522b\\u4e3e\\u4f8b\\u8bf4\\u660e\\u3002\\u5148\\u6765\\u770b\\u770b\\u901a\\u8fc7\\u7ee7\\u627fthreading.Thread\\u7c7b\\u6765\\u521b\\u5efa\\u7ebf\\u7a0b\\u7684\\u4f8b\\u5b50\\uff1a</p><!-- Crayon Syntax Highlighter v2.7.1.1 -->\\n<div data-settings=\\\" minimize scroll-alway\", \"id\": 51, \"title\": \"Python\\u6a21\\u5757\\u5b66\\u4e60\\uff1athreading \\u591a\\u7ebf\\u7a0b\\u63a7\\u5236\\u548c\\u5904\\u7406 - Python - \\u4f2f\\u4e50\\u5728\\u7ebf\"}], \"statusCode\": 200}";
                                    //System.out.println("!!!!!!!!!!" + InputRecommendID);
                                    if (DeafaultRecomned) {
                                        recommenddata = gson.fromJson(jsondata, RecommendData.class);
                                    } else {
                                        FileWriter fwriter = null;
                                        try {
                                            String getData = new String();
                                            if (OAuthRecommedID) {
                                                System.out.println("OAuth+InputRecommendID");
                                                getData = RecommendPagesCrawl.Crawlrun(OAuth.getUserId(), Integer.parseInt(RecommendPagesNumber));
                                            } else {
                                                System.out.println("InputRecommendID");
                                                getData = RecommendPagesCrawl.Crawlrun(InputRecommendID, Integer.parseInt(RecommendPagesNumber));
                                            }
                                            
                                            //寫入檔案
                                            File saveFile = new File(System.getProperty("user.dir") + "\\RecommendPagesData" + "\\RecommendPages.txt");
                                            fwriter = new FileWriter(saveFile);
                                            BufferedWriter output = new BufferedWriter(new FileWriter(saveFile));
                                            output.write(getData);
                                            output.close();
                                            fwriter.write(getData);
                                            fwriter.close();
                                            
                                            recommenddata = gson.fromJson(getData, RecommendData.class);
                                        } catch (IOException ex) {
                                            Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                        } finally {
                                            try {
                                                fwriter.close();
                                            } catch (IOException ex) {
                                                Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                    //System.out.println("!!!!!!!!!!");

                                    //for test
                                    if (recommenddata.getPagesSize() >= Integer.parseInt(RecommendPagesNumber)) {
                                        PagesNumber = Integer.parseInt(RecommendPagesNumber);
                                    } else if (Integer.parseInt(RecommendPagesNumber) > recommenddata.getPagesSize()) {
                                        PagesNumber = recommenddata.getPagesSize();
                                    }
                                    //System.out.println(recommenddata.getDescript());
                                    /*String jsondata = "{\"descript\": \"SUCESS\", \"statusCode\": 200, \"pages\": [{\"title\": \"(1) Facebook\", \"url\": \"https://www.facebook.com/\", \"id\": 1, \"descr\": \"<p>\\u00e8\\u00ab\\u008b\\u00e5\\u0095\\u009f\\u00e5\\u008b\\u0095\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00e7\\u009a\\u0084 JavaScript \\u00e6\\u0088\\u0096\\u00e6\\u0098\\u00af\\u00e5\\u008d\\u0087\\u00e7\\u00b4\\u009a\\u00e6\\u0088\\u0090\\u00e5\\u008f\\u00af\\u00e5\\u009f\\u00b7\\u00e8\\u00a1\\u008c JavaScript \\u00e7\\u009a\\u0084\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00ef\\u00bc\\u008c\\u00e4\\u00bb\\u00a5\\u00e4\\u00be\\u00bf\\u00e8\\u00a8\\u00bb\\u00e5\\u0086\\u008a Facebook\\u00e3\\u0080\\u0082</p>\"}, {\"title\": \"Roundcube Webmail :: \\u6b61\\u8fce\\u4f7f\\u7528 Roundcube Webmail\", \"url\": \"https://webmail.gandi.net/\", \"id\": 106, \"descr\": \"\\n<p></p>\\n<noscript>\\n<p>Warning: This webmail service requires Javascript! In order to use it please enable Javascript in your browser's settings.</p>\\n</noscript>\\n\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8\", \"id\": 2, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/DefaultPage.aspx?Menu=Default&LogExcute=Y\", \"id\": 3, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?LogExcute=Y&Menu=Pot\", \"id\": 6, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/TMat/Materials_S.aspx?Menu=Mat\", \"id\": 8, \"descr\": \"\"}, {\"title\": \"\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\", \"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/Search/SearchResult.aspx\", \"id\": 18, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"id\": 19, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/IFrameSub.aspx?SysURL=https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"id\": 20, \"descr\": \"\"}, {\"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\", \"url\": \"https://www.cloudflare.com/a/overview/sappy5678.com.tw\", \"id\": 21, \"descr\": \"\"}, {\"title\": \"portalx.yzu.edu.tw\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?Menu=New\", \"id\": 22, \"descr\": \"\"}, {\"title\": \"Pull Requests \\u00b7 sappy5678/Orientan\", \"url\": \"https://github.com/sappy5678/Orientan/pulls\", \"id\": 26, \"descr\": \"\"}, {\"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\", \"url\": \"https://www.cloudflare.com/a/dns/sappy5678.com.tw\", \"id\": 33, \"descr\": \"\"}, {\"title\": \"QQ\\u5e10\\u53f7\\u5b89\\u5168\\u767b\\u5f55\", \"url\": \"https://graph.qq.com/oauth/show?which=Login&display=pc&response_type=code&client_id=100270989&redirect_uri=https%3A%2F%2Fpassport.csdn.net%2Faccount%2Flogin%3Foauth_provider%3DQQProvider&state=test\", \"id\": 41, \"descr\": \"\"}, {\"title\": \"24\\u5c0f\\u6642\\u8cb7\\u5361\\u301024hbuycard\\u3011\\u8d85\\u5546\\u7e73\\u8cbb\\u81ea\\u52d5\\u767c\\u5361-steam google apple dmm \\u9322\\u5305\\u4ee3\\u78bc\\u5132\\u503c\\u79ae\\u7269\\u5361\\u514c\\u63db/\\u65e5\\u5e63\\u7f8e\\u5143\\u53f0\\u5e63\", \"url\": \"http://www.24hbuycard.com/\", \"id\": 48, \"descr\": \"\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=python+thread+%E6%95%99%E5%AD%B8\", \"id\": 57, \"descr\": \"\"}, {\"title\": \"24\\u5c0f\\u6642\\u8cb7\\u5361\\u301024hbuycard\\u3011\\u8d85\\u5546\\u7e73\\u8cbb\\u81ea\\u52d5\\u767c\\u5361- Steam \\u9322\\u5305\\u4ee3\\u78bc\\u5132\\u503c\\u53f0\\u5e63\\u7f8e\\u5143\\u514c\\u63db\\u6559\\u5b78Steam Wallet \\u514d\\u4fe1\\u7528\\u5361\", \"url\": \"http://www.24hbuycard.com/steam\", \"id\": 77, \"descr\": \"\"}, {\"title\":\"python multithreading wait till all threads finished - Stack Overflow\", \"url\": \"http://stackoverflow.com/questions/11968689/python-multithreading-wait-till-all-threads-finished\", \"id\": 79, \"descr\": \"\"}, {\"title\": \"mv \\u9664\\u4e86\\u81ea\\u5df1 - Google \\u641c\\u5c0b\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=mv+%E9%99%A4%E4%BA%86\", \"id\": 89, \"descr\": \"\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=restful+flask\", \"id\": 90, \"descr\": \"\"}]}";
                                    String a = "";*/
 /*{
                                        try {
                                            //recommenddata = gson.fromJson(Recommendpagescrawl.Crawlrun("TestUser", 20), RecommendData.class);
                                            //recommenddata = gson.fromJson("{\"descript\": \"SUCESS\", \"statusCode\": 200, \"pages\": [{\"title\": \"(1) Facebook\", \"url\": \"https://www.facebook.com/\", \"id\": 1, \"descr\": \"<p>\\u00e8\\u00ab\\u008b\\u00e5\\u0095\\u009f\\u00e5\\u008b\\u0095\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00e7\\u009a\\u0084 JavaScript \\u00e6\\u0088\\u0096\\u00e6\\u0098\\u00af\\u00e5\\u008d\\u0087\\u00e7\\u00b4\\u009a\\u00e6\\u0088\\u0090\\u00e5\\u008f\\u00af\\u00e5\\u009f\\u00b7\\u00e8\\u00a1\\u008c JavaScript \\u00e7\\u009a\\u0084\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00ef\\u00bc\\u008c\\u00e4\\u00bb\\u00a5\\u00e4\\u00be\\u00bf\\u00e8\\u00a8\\u00bb\\u00e5\\u0086\\u008a Facebook\\u00e3\\u0080\\u0082</p>\"}, {\"title\": \"Roundcube Webmail :: \\u6b61\\u8fce\\u4f7f\\u7528 Roundcube Webmail\", \"url\": \"https://webmail.gandi.net/\", \"id\": 106, \"descr\": \"\\n<p></p>\\n<noscript>\\n<p>Warning: This webmail service requires Javascript! In order to use it please enable Javascript in your browser's settings.</p>\\n</noscript>\\n\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8\", \"id\": 2, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/DefaultPage.aspx?Menu=Default&LogExcute=Y\", \"id\": 3, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?LogExcute=Y&Menu=Pot\", \"id\": 6, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/TMat/Materials_S.aspx?Menu=Mat\", \"id\": 8, \"descr\": \"\"}, {\"title\": \"\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\", \"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/Search/SearchResult.aspx\", \"id\": 18, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"id\": 19, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/IFrameSub.aspx?SysURL=https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"id\": 20, \"descr\": \"\"}, {\"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\", \"url\": \"https://www.cloudflare.com/a/overview/sappy5678.com.tw\", \"id\": 21, \"descr\": \"\"}, {\"title\": \"portalx.yzu.edu.tw\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?Menu=New\", \"id\": 22, \"descr\": \"\"}, {\"title\": \"Pull Requests \\u00b7 sappy5678/Orientan\", \"url\": \"https://github.com/sappy5678/Orientan/pulls\", \"id\": 26, \"descr\": \"\"}, {\"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\", \"url\": \"https://www.cloudflare.com/a/dns/sappy5678.com.tw\", \"id\": 33, \"descr\": \"\"}, {\"title\": \"QQ\\u5e10\\u53f7\\u5b89\\u5168\\u767b\\u5f55\", \"url\": \"https://graph.qq.com/oauth/show?which=Login&display=pc&response_type=code&client_id=100270989&redirect_uri=https%3A%2F%2Fpassport.csdn.net%2Faccount%2Flogin%3Foauth_provider%3DQQProvider&state=test\", \"id\": 41, \"descr\": \"\"}, {\"title\": \"24\\u5c0f\\u6642\\u8cb7\\u5361\\u301024hbuycard\\u3011\\u8d85\\u5546\\u7e73\\u8cbb\\u81ea\\u52d5\\u767c\\u5361-steam google apple dmm \\u9322\\u5305\\u4ee3\\u78bc\\u5132\\u503c\\u79ae\\u7269\\u5361\\u514c\\u63db/\\u65e5\\u5e63\\u7f8e\\u5143\\u53f0\\u5e63\", \"url\": \"http://www.24hbuycard.com/\", \"id\": 48, \"descr\": \"\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=python+thread+%E6%95%99%E5%AD%B8\", \"id\": 57, \"descr\": \"\"}, {\"title\": \"24\\u5c0f\\u6642\\u8cb7\\u5361\\u301024hbuycard\\u3011\\u8d85\\u5546\\u7e73\\u8cbb\\u81ea\\u52d5\\u767c\\u5361- Steam \\u9322\\u5305\\u4ee3\\u78bc\\u5132\\u503c\\u53f0\\u5e63\\u7f8e\\u5143\\u514c\\u63db\\u6559\\u5b78Steam Wallet \\u514d\\u4fe1\\u7528\\u5361\", \"url\": \"http://www.24hbuycard.com/steam\", \"id\": 77, \"descr\": \"\"}, {\"title\":\"python multithreading wait till all threads finished - Stack Overflow\", \"url\": \"http://stackoverflow.com/questions/11968689/python-multithreading-wait-till-all-threads-finished\", \"id\": 79, \"descr\": \"\"}, {\"title\": \"mv \\u9664\\u4e86\\u81ea\\u5df1 - Google \\u641c\\u5c0b\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=mv+%E9%99%A4%E4%BA%86\", \"id\": 89, \"descr\": \"\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=restful+flask\", \"id\": 90, \"descr\": \"\"}]}";
                                            JSONObject jsonobject = new JSONObject(jsondata);
                                            a = jsonobject.toString();
                                            System.out.println(a);
                                        } catch (JSONException ex) {
                                            Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }*/

                                    Hyperlink[] link = new Hyperlink[PagesNumber];
                                    String[] linkname = new String[PagesNumber];
                                    final String[] url = new String[PagesNumber];
                                    for (int i = 0; i < PagesNumber; i++) {
                                        url[i] = recommenddata.getPages().get(i).getUrl();
                                        if (recommenddata.getPages().get(i).getTitle().equals("")) {
                                            linkname[i] = recommenddata.getPages().get(i).getUrl();
                                        } else {
                                            linkname[i] = recommenddata.getPages().get(i).getTitle();
                                        }
                                        System.out.println(recommenddata.getPages().get(i).getUrl());
                                        System.out.println(recommenddata.getPages().get(i).getTitle());
                                    }
                                    /*linkname[0] = "Youtube";
                                    linkname[1] = "Yahoo";
                                    url[0] = "https://www.youtube.com/?gl=TW&hl=zh-TW";
                                    url[1] = "https://tw.yahoo.com/";*/

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
                                            tempst.initStyle(StageStyle.TRANSPARENT);
                                            StackPane tempWebroot = new StackPane();
                                            Scene tempweb = new Scene(tempWebroot);
                                            WebView tempwebView = new WebView();
                                            WebEngine tempengine = new WebEngine();
                                            tempengine = tempwebView.getEngine();
                                            mouseDetect.updateMouseData(mouseevent);
                                            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                            /* primScreenBounds.getMinX();
                                            primScreenBounds.getMaxX();*/
                                            tempst.setHeight(primScreenBounds.getMaxY() / 2 - 10);
                                            tempst.setWidth(primScreenBounds.getMaxX() / 2);
                                            //System.out.println(tempst.getWidth());
                                            if (mouseDetect.getNewX() + tempst.getWidth() > primScreenBounds.getMaxX()) {
                                                tempst.setWidth(mouseDetect.getNewX() - 20);
                                                tempst.setX(mouseDetect.getNewX() - tempst.getWidth() - 30);
                                            } else {
                                                tempst.setWidth(primScreenBounds.getMaxX() - 30);
                                                tempst.setX(mouseDetect.getNewX() + 30);
                                            }
                                            if (mouseDetect.getNewY() + tempst.getHeight() > primScreenBounds.getMaxY()) {
                                                tempst.setHeight(mouseDetect.getNewY() - 10);
                                                tempst.setY(mouseDetect.getNewY() - tempst.getHeight() - 10 - 20);
                                            } else {
                                                tempst.setHeight(primScreenBounds.getMaxY() - 10 - 20);
                                                tempst.setY(mouseDetect.getNewY() + 20);
                                            }
                                            tempengine.load(tempurl);
                                            tempWebroot.getChildren().add(tempwebView);
                                            tempst.setTitle(Localelanguage.getStringLocalelanguage("preview"));
                                            tempst.setScene(tempweb);
                                            tempst.show();
                                            templink.setOnMouseExited((MouseEvent e) -> {
                                                tempwebView.getEngine().load(null);
                                                tempst.close();

                                            });
                                        });
                                    }
                                    box.getChildren().addAll(link);
                                    scroll.setContent(box);
                                    vbox.getChildren().addAll(scroll);

                                    // Webroot.getChildren().add(webView);
                                    WebRecom.setTitle(Localelanguage.getStringLocalelanguage("Web_recommend"));
                                    WebRecom.setScene(WebRe);
                                    WebRecom.show();
                                    break;

                                case "No Ceiling":
                                    System.out.println("1");

                                    break;
                                case "sit/stand":

                                    if (actionMode.getActionMode() == 1 || actionMode.getActionMode() == 2) {
                                        if (actionMode.getActionMode() == 1) {
                                            animationManger.StopAll();
                                            actionMode.setActionMode(2);
                                            sitAction.play(1);
                                        } else if (actionMode.getActionMode() == 2) {
                                            animationManger.StopAll();
                                            actionMode.setActionMode(1);
                                            standAction.play(1);
                                        }

                                    }

                                    break;
                                case "delete":
                                    MascotThreadNumberManager.deleteOneThread();
                                    if (MascotThreadNumberManager.isZero()) {
                                        System.exit(0);
                                    } else {
                                        mascotStage.close();
                                    }
                                    /*
                                    System.out.println("delete");      
                                    Thread.currentThread().interrupt();*/
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
        root.getChildren()
                .add(MascotimageView);

    }

//設定事件結束
}
