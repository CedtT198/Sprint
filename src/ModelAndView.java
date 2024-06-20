package util;

import java.util.HashMap;

public class ModelAndView {
    String url;
    HashMap<String, Object> data;
    
    public ModelAndView() {
        this.url = "";
        this.data = new HashMap<>();
    }
    public ModelAndView(String url) {
        this.url = url;
        this.data = new HashMap<>();
    }
    
    public void addObject(String name, Object value) {
        this.data.put(name, value);
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public HashMap<String, Object> getData() {
        return data;
    }
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }    
}