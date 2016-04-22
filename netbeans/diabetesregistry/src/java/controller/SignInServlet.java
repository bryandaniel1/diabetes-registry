/* 
 * Copyright 2016 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controller;

import clinic.User;
import data.SignInIO;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.HashAndSaltUtil;

/**
 * Coordinates the sign in activity
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class SignInServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean validUser = false;
        String url;
        String message = null;
        String action = request.getParameter("action");

        if (action == null) {
            action = "Go to sign-in page.";
        }

        switch (action) {
            case "Go to sign-in page.":
                session.setAttribute("user", null);
                url = "/index.jsp";
                break;
            case "signin_user":
                String userName = request.getParameter("userName");
                String password = request.getParameter("password");
                if ((userName == null) || (userName.trim().length() == 0)) {
                    message = "The user name must be entered.";
                    request.setAttribute("errorMessage", message);
                    url = "/error.jsp";
                    break;
                } else if ((password == null) || (password.trim().length() == 0)) {
                    message = "The password must be entered.";
                    request.setAttribute("errorMessage", message);
                    url = "/error.jsp";
                    break;
                }
                String salt = SignInIO.retrieveSalt(userName);
                String userPassword = null;
                try {
                    userPassword = HashAndSaltUtil.hashBrownsWithSalt(password, salt);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(SignInServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (userPassword != null) {
                    validUser = SignInIO.validateUser(userName, userPassword);
                }
                if (validUser) {
                    User user = SignInIO.getUser(userName);
                    session.setAttribute("user", user);
                    request.setAttribute("message", message);
                    Cookie c = new Cookie("user", user.getUserName());
                    c.setMaxAge(60 * 60 * 24 * 365 * 2);    //set age to 2 years
                    c.setPath("/");
                    response.addCookie(c);

                    url = "/diabetesregistry/welcome?success=1";
                } else {
                    message = "Sign in was not successful.";
                    session.setAttribute("user", null);
                    request.setAttribute("message", message);
                    url = "/error.jsp";
                }
                break;
            case "home":
                url = "/home/index.jsp";
                break;
            default:
                message = "Sign in was not successful.";
                session.setAttribute("user", null);
                request.setAttribute("message", message);
                url = "/error.jsp";
                break;
        }
        if (validUser) {
            response.sendRedirect(url);
        } else {
            getServletContext().getRequestDispatcher(url)
                    .forward(request, response);
        }
    }
}
