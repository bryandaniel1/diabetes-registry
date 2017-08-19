/*
 * Copyright 2017 Bryan Daniel.
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

import data.PasswordManagementDataAccess;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.HashAndSaltUtility;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the task of saving new passwords.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class PasswordManagementServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 9089062892769496193L;

    /**
     * This method handles the HTTP <code>GET</code> method. All request are
     * sent to the doPost method.
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
     * This method handles the HTTP <code>POST</code> method. When the
     * saveNewPassword action is received, the form input is validated before
     * processing the new password with the HashAndSaltUtility class and storing
     * the result with the data-access class.
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
        String url = "/passwordmanagement/index.jsp";
        String userName = (String) session.getAttribute(SessionObjectUtility.PASSWORD_MANAGEMENT_KEY);
        String message;
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "saveNewPassword":
                String password = request.getParameter("newpassword");
                String repeatPassword = request.getParameter("repeatpassword");

                /* Verifying the password matches the required patern */
                boolean passwordValid = StringUtility.passwordCheck(password);

                if (!passwordValid) {
                    message = "The password does not meet the password requirements.";
                    request.setAttribute("errorMessage", message);
                } else if (!password.equals(repeatPassword)) {
                    message = "The two password entries do not match.";
                    request.setAttribute("errorMessage", message);
                } else {
                    boolean credentialsAdded = false;

                    String userSalt = HashAndSaltUtility.getSalt();
                    String hash = null;

                    try {
                        hash = HashAndSaltUtility.hashWithSalt(password, userSalt);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(PasswordManagementServlet.class.getName()).log(Level.SEVERE,
                                "The password hashing operation in PasswordManagementServlet was unsuccessful.", ex);
                    }

                    if (hash != null) {
                        credentialsAdded = PasswordManagementDataAccess
                                .changeUserPassword(userName, hash, userSalt);
                    }

                    if (credentialsAdded) {
                        message = "Your password has been successfully updated.  "
                                + "You may now sign in to the registry.";
                        session.invalidate();
                        request.setAttribute("message", message);
                        url = "/index.jsp";
                    } else {
                        message = "The password update could not be performed.  "
                                + "Contact your administrator if this operation is required.";
                        session.invalidate();
                        request.setAttribute("errorMessage", message);
                        url = "/error.jsp";
                    }
                }
                break;
            default:
                break;
        }
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
