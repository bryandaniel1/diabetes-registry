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

import data.RegistrationIO;
import data.SignInIO;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.HashAndSaltUtil;
import util.StringUtil;

/**
 * This HttpServlet class coordinates the activities of the user registration
 * page.
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class RegistrationServlet extends HttpServlet {

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
     * navigation of the registration page and processes the requests for
     * registrations of new and existing users.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/register/index.jsp";
        String action = request.getParameter("action");
        String message = null;
        String key;
        String userName;
        String password;
        String firstName;
        String lastName;
        String jobTitle;
        String id;
        Integer clinicId;
        String clinicSalt;
        String clinicKey;
        Integer matchedId;

        if (action == null) {
            action = "Go to register page.";
        }

        switch (action) {
            case "Go to register page.":
                if (request.getRequestURI().contains("existinguser")) {
                    url = "/existinguser/index.jsp";
                } else {
                    url = "/register/index.jsp";
                }
                break;
            case "register":
                url = "/error.jsp";
                key = request.getParameter("registrationKey");
                userName = request.getParameter("userName");
                password = request.getParameter("password");
                firstName = request.getParameter("firstName");
                lastName = request.getParameter("lastName");
                jobTitle = request.getParameter("jobTitle");
                id = request.getParameter("clinicId");

                /* Retrieving the integer from the clinic id parameter */
                try {
                    clinicId = Integer.parseInt(id);
                } catch (NumberFormatException nfe) {
                    clinicId = null;
                }

                /* Getting the matching clinic id for the given key */
                clinicSalt = RegistrationIO.retrieveClinicSalt(clinicId);
                clinicKey = null;
                try {
                    clinicKey = HashAndSaltUtil.hashBrownsWithSalt(key, clinicSalt);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                matchedId = null;
                if (null != clinicKey) {
                    matchedId = RegistrationIO.checkRegistrationKey(clinicKey);
                }

                /* Verifying the password matches the required patern */
                boolean passwordValid = StringUtil.passwordCheck(password);
                if (clinicId == null) {
                    message = "Invalid registration attempt.";

                } else if ((null == matchedId) || (!matchedId.equals(clinicId))) {
                    message = "Invalid registration attempt.";

                } else if ((userName == null) || (userName.trim().length() == 0)) {
                    message = "All fields are required.";

                } else if (!passwordValid) {
                    message = "The password does not meet the password requirements.";

                } else if ((firstName == null) || (firstName.trim().length() == 0)) {
                    message = "All fields are required.";

                } else if ((lastName == null) || (lastName.trim().length() == 0)) {
                    message = "All fields are required.";

                } else if ((jobTitle == null) || (jobTitle.trim().length() == 0)) {
                    message = "All fields are required.";

                } else {

                    if (!StringUtil.tooLongForShortVarChar(userName)) {
                        boolean credentialsAdded = false;

                        String userSalt = HashAndSaltUtil.getSalt();
                        String userPassword = null;

                        try {
                            userPassword = HashAndSaltUtil.hashBrownsWithSalt(password, userSalt);
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (userPassword != null) {
                            credentialsAdded = RegistrationIO
                                    .registerUser(userName, firstName, lastName, jobTitle,
                                            userPassword, userSalt, clinicKey);
                        }

                        if (credentialsAdded) {
                            message = "Registration has been successfully completed.";

                            url = "/index.jsp";
                        } else {
                            message = "Registration could not be completed.";
                        }
                    } else {
                        message = "Registration could not be completed.";
                    }
                }
                request.setAttribute("message", message);
                break;
            case "registerExistingUser":
                url = "/error.jsp";
                key = request.getParameter("registrationKey");
                userName = request.getParameter("userName");
                password = request.getParameter("password");
                if (((userName == null) || (userName.trim().length() == 0))
                        || ((password == null) || (password.trim().length() == 0))
                        || ((key == null) || (key.trim().length() == 0))) {
                    message = "All fields are required.";
                    break;
                }
                id = request.getParameter("clinicId");
                String userPassword = null;

                /* Retrieving the integer from the clinic id parameter */
                try {
                    clinicId = Integer.parseInt(id);
                } catch (NumberFormatException nfe) {
                    message = "Clinic ID is invalid.";
                    break;
                }

                /* Getting the matching clinic id for the given key */
                clinicSalt = RegistrationIO.retrieveClinicSalt(clinicId);
                clinicKey = null;
                try {
                    clinicKey = HashAndSaltUtil.hashBrownsWithSalt(key, clinicSalt);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                matchedId = null;
                if (null != clinicKey) {
                    matchedId = RegistrationIO.checkRegistrationKey(clinicKey);
                }
                if ((null == matchedId) || (!matchedId.equals(clinicId))) {
                    message = "Invalid registration attempt.";

                } else {
                    boolean validUser = false;
                    String salt = SignInIO.retrieveSalt(userName);
                    try {
                        userPassword = HashAndSaltUtil.hashBrownsWithSalt(password, salt);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(SignInServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (userPassword != null) {
                        validUser = SignInIO.validateUser(userName, userPassword);
                    }
                    if (validUser) {
                        boolean credentialsAdded = RegistrationIO
                                .registerExistingUser(userName, userPassword, clinicKey);

                        if (credentialsAdded) {
                            message = "Registration has been successfully completed.";

                            url = "/index.jsp";
                        } else {
                            message = "Registration could not be completed.";
                        }
                    } else {
                        message = "Registration could not be completed.";
                    }
                }
                break;
        }
        request.setAttribute("message", message);
        getServletContext().getRequestDispatcher(url)
                .forward(request, response);
    }
}
