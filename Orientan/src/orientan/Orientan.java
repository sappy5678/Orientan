/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan;

import java.awt.AWTException;
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

/**
 *
 * @author user
 */
public class Orientan extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("OrientanFXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void setIcon(Stage stage) {
        TrayIcon trayIcon = null;
         if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("D:\\DATA\\hw\\JAVA\\testProject\\testFXML\\icon.png");
            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // execute default action of the application
                    // ...
                }

            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem();
            popup.addActionListener(listener);
            //popup.add(defaultItem);

            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Tray Demo", popup);
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
    
}
