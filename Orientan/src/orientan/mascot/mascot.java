package orientan.mascot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private Boolean DeafaultRecomned = true;
    private Boolean DeafaultRecomnedID = true;
    private Gson gson = new Gson();
    private RecommendData recommenddata = new RecommendData();

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
                    webcommand.setText("Web Command");
                    CheckMenuItem noCeilingBoolean = new CheckMenuItem();
                    noCeilingBoolean.setId("No Ceiling");
                    noCeilingBoolean.setText("No Ceiling");
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
                    ClimbMode.setText("ClimbMode On/Off");
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
                    sitStandMode.setText("sit/stand");
                    MenuItem delete = new MenuItem();
                    delete.setId("delete");
                    delete.setText("delete");
                    MenuItem exit = new MenuItem();

                    exit.setId("Exit");
                    exit.setText("Exit");

                    ContextMenu contextmenu = new ContextMenu(webcommand, noCeilingBoolean, ClimbMode, sitStandMode, delete, exit);

                    contextmenu.setOnAction(
                            new EventHandler<ActionEvent>() {
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

                                    //網頁推薦數量視窗
                                    if (RecommendPagesNumber == null) {
                                        final ChoiceDialog<String> choiceDialog = new ChoiceDialog("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
                                        choiceDialog.setTitle("推薦網頁數選擇"); //設定對話框視窗的標題列文字
                                        choiceDialog.setHeaderText("歡迎使用推薦網頁功能，選取推薦網頁數之後我將會記住你的選擇呦~"); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                                        choiceDialog.setContentText("請選取推薦網頁數量："); //設定對話框的訊息文字
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
                                    }

                                    //
                                    String jsondata = "{\"descript\": \"SUCESS\", \"statusCode\": 200, \"pages\": [{\"title\": \"(1) Facebook\", \"url\": \"https://www.facebook.com/\", \"id\": 1, \"descr\": \"<p>\\u00e8\\u00ab\\u008b\\u00e5\\u0095\\u009f\\u00e5\\u008b\\u0095\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00e7\\u009a\\u0084 JavaScript \\u00e6\\u0088\\u0096\\u00e6\\u0098\\u00af\\u00e5\\u008d\\u0087\\u00e7\\u00b4\\u009a\\u00e6\\u0088\\u0090\\u00e5\\u008f\\u00af\\u00e5\\u009f\\u00b7\\u00e8\\u00a1\\u008c JavaScript \\u00e7\\u009a\\u0084\\u00e7\\u0080\\u008f\\u00e8\\u00a6\\u00bd\\u00e5\\u0099\\u00a8\\u00ef\\u00bc\\u008c\\u00e4\\u00bb\\u00a5\\u00e4\\u00be\\u00bf\\u00e8\\u00a8\\u00bb\\u00e5\\u0086\\u008a Facebook\\u00e3\\u0080\\u0082</p>\"}, {\"title\": \"Roundcube Webmail :: \\u6b61\\u8fce\\u4f7f\\u7528 Roundcube Webmail\", \"url\": \"https://webmail.gandi.net/\", \"id\": 106, \"descr\": \"\\n<p></p>\\n<noscript>\\n<p>Warning: This webmail service requires Javascript! In order to use it please enable Javascript in your browser's settings.</p>\\n</noscript>\\n\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8\", \"id\": 2, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/DefaultPage.aspx?Menu=Default&LogExcute=Y\", \"id\": 3, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?LogExcute=Y&Menu=Pot\", \"id\": 6, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/TMat/Materials_S.aspx?Menu=Mat\", \"id\": 8, \"descr\": \"\"}, {\"title\": \"\\u5143\\u667a\\u5927\\u5b78--\\u9928\\u85cf\\u8cc7\\u6e90\", \"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/Search/SearchResult.aspx\", \"id\": 18, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"id\": 19, \"descr\": \"\"}, {\"title\": \"\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/IFrameSub.aspx?SysURL=https://lib.yzu.edu.tw/ajaxYZlib/PersonLogin/Default.aspx?PassURL=/UserLoan/PersonalLoan.aspx\", \"id\": 20, \"descr\": \"\"}, {\"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\", \"url\": \"https://www.cloudflare.com/a/overview/sappy5678.com.tw\", \"id\": 21, \"descr\": \"\"}, {\"title\": \"portalx.yzu.edu.tw\", \"url\": \"https://portalx.yzu.edu.tw/PortalSocialVB/FMain/PostWall.aspx?Menu=New\", \"id\": 22, \"descr\": \"\"}, {\"title\": \"Pull Requests \\u00b7 sappy5678/Orientan\", \"url\": \"https://github.com/sappy5678/Orientan/pulls\", \"id\": 26, \"descr\": \"\"}, {\"title\": \"DNS: sappy5678.com.tw | Cloudflare - Web Performance & Security\", \"url\": \"https://www.cloudflare.com/a/dns/sappy5678.com.tw\", \"id\": 33, \"descr\": \"\"}, {\"title\": \"QQ\\u5e10\\u53f7\\u5b89\\u5168\\u767b\\u5f55\", \"url\": \"https://graph.qq.com/oauth/show?which=Login&display=pc&response_type=code&client_id=100270989&redirect_uri=https%3A%2F%2Fpassport.csdn.net%2Faccount%2Flogin%3Foauth_provider%3DQQProvider&state=test\", \"id\": 41, \"descr\": \"\"}, {\"title\": \"24\\u5c0f\\u6642\\u8cb7\\u5361\\u301024hbuycard\\u3011\\u8d85\\u5546\\u7e73\\u8cbb\\u81ea\\u52d5\\u767c\\u5361-steam google apple dmm \\u9322\\u5305\\u4ee3\\u78bc\\u5132\\u503c\\u79ae\\u7269\\u5361\\u514c\\u63db/\\u65e5\\u5e63\\u7f8e\\u5143\\u53f0\\u5e63\", \"url\": \"http://www.24hbuycard.com/\", \"id\": 48, \"descr\": \"\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=python+thread+%E6%95%99%E5%AD%B8\", \"id\": 57, \"descr\": \"\"}, {\"title\": \"24\\u5c0f\\u6642\\u8cb7\\u5361\\u301024hbuycard\\u3011\\u8d85\\u5546\\u7e73\\u8cbb\\u81ea\\u52d5\\u767c\\u5361- Steam \\u9322\\u5305\\u4ee3\\u78bc\\u5132\\u503c\\u53f0\\u5e63\\u7f8e\\u5143\\u514c\\u63db\\u6559\\u5b78Steam Wallet \\u514d\\u4fe1\\u7528\\u5361\", \"url\": \"http://www.24hbuycard.com/steam\", \"id\": 77, \"descr\": \"\"}, {\"title\":\"python multithreading wait till all threads finished - Stack Overflow\", \"url\": \"http://stackoverflow.com/questions/11968689/python-multithreading-wait-till-all-threads-finished\", \"id\": 79, \"descr\": \"\"}, {\"title\": \"mv \\u9664\\u4e86\\u81ea\\u5df1 - Google \\u641c\\u5c0b\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=mv+%E9%99%A4%E4%BA%86\", \"id\": 89, \"descr\": \"\"}, {\"title\": \"Google\", \"url\": \"https://www.google.com/webhp?sourceid=chrome-instant&rlz=1C1MSNA_enTW701TW701&ion=1&espv=2&ie=UTF-8#safe=off&q=restful+flask\", \"id\": 90, \"descr\": \"\"}]}";
                                    System.out.println("!!!!!!!!!!");
                                    if (DeafaultRecomned) {
                                        recommenddata = gson.fromJson(jsondata, RecommendData.class);
                                    } else if (DeafaultRecomnedID) {
                                        FileWriter fwriter = null;
                                try {
                                    String getData = RecommendPagesCrawl.Crawlrun("TestUser", Integer.parseInt(RecommendPagesNumber));
                                    //寫入檔案
                                    File saveFile = new File(System.getProperty("user.dir") + "\\RecommendPagesData" + "\\RecommendPages.txt");
                                    fwriter = new FileWriter(saveFile);
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
                                    } else {
                                        try {

                                            recommenddata = gson.fromJson(RecommendPagesCrawl.Crawlrun(OAuth.getUserId(), Integer.parseInt(RecommendPagesNumber)), RecommendData.class);
                                        } catch (IOException ex) {
                                            Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    System.out.println("!!!!!!!!!!");
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

                                    Hyperlink[] link = new Hyperlink[recommenddata.getPagesSize()];
                                    String[] linkname = new String[recommenddata.getPagesSize()];
                                    final String[] url = new String[recommenddata.getPagesSize()];
                                    for (int i = 0; i < recommenddata.getPagesSize(); i++) {
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
