package security;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    class User {
        private String username;
        private String password;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // User user = authentification(username, password);
        
        // if (user != null) {
        //     request.getSession().setAttribute("user", user);
        //     request.getSession().setAttribute("userRoles", user.getRoles());
            
        //     response.sendRedirect("home.jsp");
        // } else {
        //     request.setAttribute("loginError", "Identifiants incorrects");
        //     request.getRequestDispatcher("loginRole.jsp").forward(request, response);
        // }
    }
}
