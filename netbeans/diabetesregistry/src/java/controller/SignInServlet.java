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

import data.PasswordManagementDataAccess;
import data.SignInDataAccess;
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
import registry.User;
import utility.HashAndSaltUtility;
import utility.SessionObjectUtility;
import utility.StringUtility;

/**
 * This HttpServlet class coordinates the sign in activity.
 *
 * @author Bryan Daniel
 * @version 2, March 16, 2017
 */
public class SignInServlet extends HttpServlet {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 7369439989732196379L;

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
     * navigation of the sign-in page and processes sign-in requests.
     *
     * The three possible outcomes expected from the authentication process are
     * authenticated, failed, or a password change required. An authenticated
     * user will be redirected to the WelcomeServlet. A failed authentication
     * will forward the user to the error page. A password change required will
     * forward the user to the password management page where the password can
     * be updated.
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
        SignInDataAccess.SignInStatus signInStatus = SignInDataAccess.SignInStatus.FAILED;
        String userName;
        String message;
        String action = request.getParameter("action");

        if (action == null) {
            action = "Go to sign-in page.";
        }

        switch (action) {
            case "Go to sign-in page.":
                session.setAttribute(SessionObjectUtility.USER, null);
                break;
            case "signin_user":
                userName = request.getParameter("userName");
                String password = request.getParameter("password");
                if ((userName == null) || (userName.trim().length() == 0)) {
                    message = "The user name must be entered.";
                    request.setAttribute("errorMessage", message);
                    url = "/error.jsp";
                    break;
                }
                if ((password == null) || (password.trim().length() == 0)) {
                    message = "The password must be entered.";
                    request.setAttribute("errorMessage", message);
                    url = "/error.jsp";
                    break;
                }
                if (StringUtility.tooLongForUsernameVarChar(userName)) {
                    message = "Sign in was not successful.";
                    session.setAttribute(SessionObjectUtility.USER, null);
                    request.setAttribute("errorMessage", message);
                    url = "/error.jsp";
                    break;
                }
                String salt = SignInDataAccess.retrieveSalt(userName);
                String hashedPassword = null;
                try {
                    hashedPassword = HashAndSaltUtility.hashWithSalt(password, salt);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(SignInServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (hashedPassword != null) {
                    signInStatus = SignInDataAccess.authenticateUser(userName, hashedPassword);
                }
                switch (signInStatus) {
                    case PASSWORD_CHANGE:
                        message = "Your password has been set temporarily. "
                                + "You must change your password to access the registry.";
                        request.setAttribute("message", message);
                        session.setAttribute(SessionObjectUtility.PASSWORD_MANAGEMENT_KEY, userName);
                        Cookie passwordChangeCookie = new Cookie("userID", userName);
                        passwordChangeCookie.setHttpOnly(true);
                        passwordChangeCookie.setSecure(true);
                        passwordChangeCookie.setMaxAge(60 * 5); //set age to 5 minutes
                        passwordChangeCookie.setPath("/");
                        response.addCookie(passwordChangeCookie);
                        url = "/changepassword";
                        break;
                    case AUTHENTICATED:
                        User user = SignInDataAccess.getUser(userName);
                        session.setAttribute(SessionObjectUtility.USER, user);
                        Cookie signedInCookie = new Cookie("user", user.getUserName());
                        signedInCookie.setHttpOnly(true);
                        signedInCookie.setSecure(true);
                        signedInCookie.setMaxAge(60 * 60 * 24 * 365 * 2); //set age to 2 years
                        signedInCookie.setPath("/");
                        response.addCookie(signedInCookie);
                        url = "/diabetesregistry/welcome?success=1";
                        break;
                    case FAILED:
                        message = "Sign in was not successful.";
                        session.setAttribute(SessionObjectUtility.USER, null);
                        request.setAttribute("errorMessage", message);
                        url = "/error.jsp";
                        break;
                    default:
                        break;
                }
                break;
            case "requestreset":
                request.setAttribute("reset", 1);
                break;
            case "sendPasswordResetRequest":
                boolean validInput = true;
                userName = request.getParameter("userName");
                String userEmail = request.getParameter("email");

                if ((userName == null) || (userName.trim().length() == 0)) {
                    message = "The user name must be entered.";
                    request.setAttribute("errorMessage", message);
                    request.setAttribute("reset", 1);
                    break;
                }
                if (StringUtility.tooLongForUsernameVarChar(userName)) {
                    validInput = false;
                }
                if ((userEmail == null) || (userEmail.trim().length() == 0)) {
                    message = "The user email address must be entered.";
                    request.setAttribute("errorMessage", message);
                    request.setAttribute("reset", 1);
                    break;
                }
                if (StringUtility.tooLongForEmailVarChar(userEmail)) {
                    validInput = false;
                }

                boolean validRequest = false;
                if (validInput) {
                    validRequest = PasswordManagementDataAccess.requestPasswordReset(userName, userEmail);
                }

                if (validRequest) {
                    message = "Your request has been sent to the administrator.";
                    request.setAttribute("message", message);
                } else {
                    message = "The information you provided does not match our records.  "
                            + "Please contact your administrator.";
                    request.setAttribute("errorMessage", message);
                    url = "/error.jsp";
                }
                break;
            default:
                message = "Sign in was not successful.";
                session.setAttribute(SessionObjectUtility.USER, null);
                request.setAttribute("message", message);
                url = "/error.jsp";
                break;
        }
        if (signInStatus == SignInDataAccess.SignInStatus.AUTHENTICATED) {
            response.sendRedirect(url);
        } else {
            getServletContext().getRequestDispatcher(url)
                    .forward(request, response);
        }
    }
}
