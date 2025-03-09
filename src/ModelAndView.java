package util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.http.HttpSession;
import util.MySession;
import jakarta.servlet.http.HttpServletRequest;

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

    // public void saveToSession(MySession session) {
    //     ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    //     for (Map.Entry<String, Object> entry : data.entrySet()) {
    //         session.add(entry.getKey(), entry.getValue());

    //         scheduler.schedule(() -> {
    //             try {
    //             session.delete(entry.getKey());
    //             System.out.println("Attribut temporaire " + entry.getKey() + " supprimé après 5 secondes.");
    //             } catch(Exception e) {
    //                 e.printStackTrace();
    //             }
    //         }, 5, TimeUnit.SECONDS);
    //     }
    //     scheduler.shutdown();
    // }

    // public void removeToSession(HttpSession session) {
    //     Enumeration<String> attributeNames = session.getAttributeNames();
    //     while (attributeNames.hasMoreElements()) {
    //         String key = attributeNames.nextElement();
    //         Object value = session.getAttribute(key);
    //         if (value != null) {
    //             System.out.println("Valeur : "+value + " / Key : " + key);
    //         }
    //     }

    //     if (this.getData() != null) {
    //         for (String key : this.getData().keySet()) {
    //             session.removeAttribute(key); 
    //         }
    //         this.getData().clear();
    //     }
    // }

    // public void removeToSession(MySession session) {
    //     Enumeration<String> attributeNames = session.getAttributeNames();
    //     while (attributeNames.hasMoreElements()) {
    //         String key = attributeNames.nextElement();
    //         Object value = session.get(key);
    //         if (value != null) {
    //             System.out.println("Valeur : "+value + " / Key : " + key);
    //         }
    //     }

    //     if (this.getData() != null) {
    //         for (String key : this.getData().keySet()) {
    //             session.delete(key); 
    //         }

    //         this.getData().clear();
    //     }
    // }
    
    public void addObject(String key, Object value) {
        this.data.put(key, value);
    }

    // public void addObject(String key, Object value, MySession session) {
    //     this.data.put(key, value);
    //     session.add(key, value);

    //     ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    //     scheduler.schedule(() -> {
    //         session.delete(key);
    //         System.out.println("Attribut temporaire "+key+" supprimé après 3 secondes.");
    //     }, 3, TimeUnit.SECONDS);

    //     scheduler.shutdown();
    // }

    // public void addObject(String key, Object value, HttpSession session) {
    //     this.data.put(key, value);
    //     session.setAttribute(key, value);

    //     ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    //     scheduler.schedule(() -> {
    //         session.removeAttribute(key);
    //         System.out.println("Attribut temporaire "+key+" supprimé après 3 secondes.");
    //     }, 3, TimeUnit.SECONDS);

    //     scheduler.shutdown();
    // }

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