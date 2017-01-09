/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.RecommendPages;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zp
 */
public final class RecommendPagesCrawl {

    private static String apiURL = "http://orientan.sappy5678.com.tw:5000/api/v1.0/RecommendPages";

    public static String Crawlrun(String userName, Integer pagesNumber) {
        String RecommendPageJson = "";
        StringBuffer bf = new StringBuffer();
        InputStreamReader ISR = null;

        int data;
        try {
            URL targetURL = new URL(apiURL + "/" + userName + "/" + pagesNumber.toString());
            URLConnection conn = targetURL.openConnection();
            System.out.println(targetURL);
            conn.connect();
            System.out.println(conn.getContentType());
            ISR = new InputStreamReader(conn.getInputStream(), "UTF-8");
            data = ISR.read();
            while (data != -1) {
                bf.append((char) data);
                data = ISR.read();
            }
            ISR.close();
            //去掉頭  "
            //bf.deleteCharAt(0);
            //去掉尾巴 "
            //System.out.println(bf.charAt(bf.length()-1));
            //bf.delete(bf.length()-2, bf.length());
            //bf.toString().replaceAll("\\\\", "");
                    //str1.replaceAll("\\\\", "");
            //System.out.println("crawl :"+bf.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(RecommendPagesCrawl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecommendPagesCrawl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ISR.close();
            } catch (IOException ex) {
                Logger.getLogger(RecommendPagesCrawl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.printf("!!!!");
        RecommendPageJson=bf.toString().replaceAll("\\\\\\\\","\\");
        //System.out.printf(RecommendPageJson);
        return RecommendPageJson;
    }
}
