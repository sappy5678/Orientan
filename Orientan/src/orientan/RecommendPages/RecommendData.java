/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.RecommendPages;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public final class RecommendData {

    private  int statusCode;
    private List<RecommendPageData> pages=new ArrayList<RecommendPageData>();
    private  String descript;
    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<RecommendPageData> getPages() {
        return pages;
    }
    public int getPagesSize()
    {
        return pages.size();
    }
    public void setPages(List<RecommendPageData> pages) {
        this.pages = pages;
    }

    
    
}
