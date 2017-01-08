/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.*;
import orientan.ImageSetChooser.ImageSetChooser;
import orientan.config.loadconfig;
import orientan.mascot.mascot;
import orientan.mascotEnvironment.Mouse;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author user
 */
public class Orientan extends Application {

    Properties properties = new Properties();
    private String imageSetPath;
    private Mouse mouseDetect;
    private loadconfig config;
    private AddMascotService addMascot;

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("OrientanFXMLDocument.fxml"));

        // set log type
        // 設定log的類別
        settingLog();
        Localelanguage.selectLanguage();
        //給thread緩衝時間
        //Thread.sleep(2000);
        // get config in orientan 
        //  取得設定檔 在  orientan 底下
        properties = getConfig();
        setIcon(stage);
        //取得動作設定檔(actions)
        config = new loadconfig("actions");
        /**/
//      
        System.out.println("a");
        String out = "";
        //out=RecommendPagesCrawl.Crawlrun("TestUser", 20);
        System.out.println(out);
        //建立桌寵環境參數

        //建立滑鼠監控
        mouseDetect = new Mouse();
        MascotThreadNumberManager.addOneThread();
        addMascot = new AddMascotService(mouseDetect, config, imageSetPath);
        addMascot.start();
           
        //imageSetPath=addMascot.returnPath();
        // mascotenvironment.setImage(new Image(new File(imageSetPath+ "\\shime1.png").toURI().toString()));
        // Notifications.create().title("Orientan Status").text("Orientan Start to Run").showInformation();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //System.out.println(ResourceBundle.getBundle("Test", Locale.getDefault()).getString("test"));
        launch(args);
    }

    // 設置系統槽的icon
    // set System tray icon
    private void setIcon(Stage stage) throws UnsupportedEncodingException {
        Locale currentLocale = Locale.getDefault();
        //ResourceBundle SystemTrayMenu = ResourceBundle.getBundle("messages", currentLocale);
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage(properties.getProperty("icon", "./img/icon/icon.png"));

            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // execute default action of the application
                    String cmd = e.getActionCommand();
                    // exit 離開
                    if (cmd == Localelanguage.getStringLocalelanguage("exit")) {
                        System.out.println(e);
                        System.exit(0);

                    }
                }

            };
            ActionListener addMascotlistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // execute default action of the application
                    String cmd = e.getActionCommand();
                    System.out.println(cmd);
                    // exit 離開
                    /*
                    switch (cmd) {
                        case "add a mascot": {
                            //new Thread(new AddMascotService(mouseDetect, config, imageSetPath)).start();
                            break;
                        }
                        case SystemTrayMenu.getString("exit"): {
                            System.out.println(e);
                            System.exit(0);
                            break;
                        }
                    }*/
                    if (cmd.equals(Localelanguage.getStringLocalelanguage("exit"))) {
                        System.out.println(e);
                        System.exit(0);

                    } else if (cmd.equals(Localelanguage.getStringLocalelanguage("Add_mascot"))) {
                        System.out.println("Add_mascot");
                        MascotThreadNumberManager.addOneThread();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                
                                new Thread(new AddMascotService(mouseDetect, config, imageSetPath)).start();

                            }

                            //AddMascotService addMascot2 = new AddMascotService(mouseDetect, config, imageSetPath);
                            //addMascot2.run();
                        });
                        
                    }
                }

            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action

            java.awt.MenuItem exit = new java.awt.MenuItem(Localelanguage.getStringLocalelanguage("exit"));
            java.awt.MenuItem Add_mascot = new java.awt.MenuItem(Localelanguage.getStringLocalelanguage("Add_mascot"));

            popup.addActionListener(addMascotlistener);
            //popup.add(test);
            // java.awt.MenuItem setNewMascot = new java.awt.MenuItem(SystemTrayMenu.getString("Add_mascot"));
            popup.add(Add_mascot);
            popup.add(exit);

            //popup.add(setNewMascot);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Orientan", popup);

            // set the TrayIcon properties
            trayIcon.addActionListener(addMascotlistener);

            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        } else {
            // disable tray option in your application or
            // perform other actions

        }
    }

    // get config in orientan 
    //  取得設定檔 在  orientan 底下
    private Properties getConfig() {
        Properties properties = new Properties();
        String configFile = "./config/config.properties";
        try {
            properties.load(new FileInputStream(configFile));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return properties;
    }

    //setting logs
    // 設置log
    private void settingLog() {
        Logger logger = Logger.getLogger("MainLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter  
            fh = new FileHandler("./logs/logs.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            logger.info("Create Logs");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Start to Run");
    }

}
