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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Contains the GET method to direct to the appropriate page a user who is
 * trying to sign in
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class WelcomeServlet extends HttpServlet {

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
        HttpSession session = request.getSession();
        String url = "/index.jsp";
        String success = request.getParameter("success");

        if (success == null) {
            success = "Go to sign-in page.";
        }

        switch (success) {
            case "Go to sign-in page.":
                session.setAttribute("user", null);
                session.setAttribute("message", "You must sign in to access the registry.");
                break;
            case "1":
                /**
                 * This key needs to be created, read from an external file, or
                 * received from the user.
                 */
                ServletContext sc = request.getServletContext();
                sc.setAttribute("referenceCharacters", "theDatabaseKey");
                url = "/welcome/index.jsp";
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }

}
