/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan;

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
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.*;
import orientan.mascot.mascot;

/**
 *
 * @author user
 */
public class Orientan extends Application {

    Properties properties = new Properties();

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("OrientanFXMLDocument.fxml"));

        // set log type
        // 設定log的類別
        settingLog();

        // get config in orientan 
        //  取得設定檔 在  orientan 底下
        properties = getConfig();
        setIcon(stage);
      /**/
      
        mascot m=new mascot();
        /*
        //Image image =new Image("user.dir/img/shime1.png");
        //ImageView iv1=new ImageView();
        //iv1.setImage(image);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        StackPane root = new StackPane();
        //設定root顏色
        root.getChildren().add(btn);
        root.setStyle("-fx-background:transparent;");
        //設定scene顏色與大小
        Scene scene = new Scene(root, 300, 250);
        scene.setFill(null);
        //連結在一起並且顯示出來
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        */
        Notifications.create().title("Orientan Status").text("Orientan Start to Run").showInformation();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    // 設置系統槽的icon
    // set System tray icon
    private void setIcon(Stage stage) throws UnsupportedEncodingException {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle SystemTrayMenu = ResourceBundle.getBundle("messages", currentLocale);
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
                    if(cmd == SystemTrayMenu.getString("exit"))
                    {
                        System.out.println(e);
                        System.exit(0);
                        
                    }
                }

            };

            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            java.awt.MenuItem test = new java.awt.MenuItem(SystemTrayMenu.getString("exit"));
            popup.addActionListener(listener);
            popup.add(test);

            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Orientan", popup);

            // set the TrayIcon properties
            trayIcon.addActionListener(listener);

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
