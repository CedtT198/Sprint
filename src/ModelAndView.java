package modelandview;

public class ModelAndView {
    String url;
    HashMap<String, Object> data;
    
    public ModelAndView() {}
    public ModelAndView(String url) {
        this.url = url;
    }
    
    public void addObject(String name, Object data) {
        this.data.put(name, data);
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