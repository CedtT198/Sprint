<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
</head>
<body>
    <center>
        <% List<String> liste_error = (List<String>) request.getAttribute("list_error");
                for (String error : liste_error) { %>
                    <div>
                        <p><% out.println(error);%></p><br>
                    </div>
        <% } %>
    </center>
</body>
</html>