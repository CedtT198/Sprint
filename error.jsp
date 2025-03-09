<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Error Runtime Exception</title>
<style>
    body {
        margin: 0;
        padding: 25px;
        color: white;
        background-color: #e6394d;
        font-family: Verdana, Geneva, Tahoma, sans-serif;
    }
    h1 {
        font-size: 50px;
        text-align: center;
        font-weight: 100;
    }
    li {
        font-size: 15px;
    }
</style>
</head>
<body>
    <div class="container">
        <h1>Error Runtime Exception</h1><br>
        <ul>
            <% List<String> errors = (List<String>) request.getAttribute("list_error");
                for (String error : errors) { %>
                    <li><% out.println(error);%></li><br>
            <% } %>
        </ul>
    </div>
</body>
</html>
