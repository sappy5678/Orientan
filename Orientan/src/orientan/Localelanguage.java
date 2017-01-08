/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author zp
 */
public final class Localelanguage {

    private static String language = null;

    //private static String key = null;
    public static String getStringLocalelanguage(String key) {
        return ResourceBundle.getBundle(language, Locale.getDefault()).getString(key);
    }

    public static void selectLanguage() {
        //修復javafx thread的問題
        Platform.setImplicitExit(false);
        //
        int startIndex = 0;
        int endIndex;
        ArrayList<String> fname = new ArrayList<String>();
        //讀取
        for (File f : new File(System.getProperty("user.dir") + "\\src").listFiles()) {
            //取得副檔名位置
            startIndex = f.getName().lastIndexOf(46) + 1; //46 小數點的ASCII
            endIndex = f.getName().length();
            //(副檔名)篩選條件
            if (!f.getName().substring(startIndex, endIndex).equals("properties")) {
                continue;
            }
            //檔名篩選
            /*
            if(!f.getName().substring(0, startIndex-1).equals("default")||!f.getName().substring(0, startIndex-1).equals("Default"))
            {
                continue;
            }*/
            //取檔名
            fname.add(f.getName().substring(0, startIndex - 1));
            //System.out.println(f);
        }
        //選擇      
        final ChoiceDialog<String> choiceDialog = new ChoiceDialog(fname.get(0), fname);
        choiceDialog.setTitle("Select language"); //設定對話框視窗的標題列文字
        choiceDialog.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        choiceDialog.setContentText("Select language:"); //設定對話框的訊息文字
        choiceDialog.showAndWait(); //顯示對話框，並等待對話框被關閉時才繼續執行之後的程式
        try {
            language = choiceDialog.getResult(); //可以直接用「choiceDialog.getResult()」來取代           
        } catch (final NoSuchElementException ex) {
            language = null;
            System.exit(0);
        }

        if (language == null) {
            //沒有選擇生肖，而是直接關閉對話框
            System.out.println("您取消了選擇，期待您下次使用");
            System.exit(0);
        }
    
    }
}
