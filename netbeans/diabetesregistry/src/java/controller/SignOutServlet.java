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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.SessionObjectUtility;

/**
 * This HttpServlet class coordinates the sign out activity.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class SignOutServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 7174245216767468823L;

    /**
     * Handles the HTTP <code>GET</code> method. This method invokes the doPost
     * method for all requests.
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
     * Handles the HTTP <code>POST</code> method. This method coordinates the
     * navigation of the sign-out page and processes sign-out requests.
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
        String url = "/index.jsp";
        String action = request.getParameter("action");

        if (action == null) {
            action = "Go to sign-in page.";
        }

        switch (action) {
            case "Go to sign-in page.":
                session.setAttribute(SessionObjectUtility.USER, null);
                break;
            case "signout":
                Cookie c = new Cookie("user", null);
                c.setMaxAge(0);    //set age to zero
                c.setPath("/");
                response.addCookie(c);
                session.invalidate();
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
