package mg.prom16.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Reflect
{
    public Reflect() {}

    public String[] getArgs(HttpServletRequest request) throws ServletException, IOException {
        String[] param;

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.size() <= 0) {
            param = null;
            System.out.println("Tsy misy");
        }
        else {
            param = new String[parameterMap.size()];

            List<String[]> valuesList = new ArrayList<>(parameterMap.values());
            for (int i = 0; i < valuesList.size(); i++) {
                String values = valuesList.get(i)[0];
                param[i] = values;
                System.out.println("Values = " + values);
            }
        }

        return param;
    }   

    public String[] getArgs(String requestURL) throws URISyntaxException {
        URI uri = new URI(requestURL);
        String query = uri.getQuery();
        if (query == null) {
            return null;
        }

        String[] pairs = query.split("&");
        String[] param = new String[pairs.length];

        for (int i = 0; i < pairs.length; i++) {
            String[] keyValue = pairs[i].split("=");
            param[i] = keyValue.length > 1 ? keyValue[1] : "";
        }

        return param;
    }   
}