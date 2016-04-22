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
package util;

import clinic.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The filter to check for administrator status en route to the administration
 * page
 *
 * @author Bryan Daniel
 * @version 1, April 8, 2016
 */
public class AdministrationFilter implements Filter {

    /**
     * Debug status
     */
    private static final boolean debug = true;

    /**
     * The filter configuration object we are associated with. If this value is
     * null, this filter instance is not currently configured.
     */
    private FilterConfig filterConfig = null;

    /**
     * Default constructor
     */
    public AdministrationFilter() {
    }

    /**
     * This filter method only allows administrators to proceed.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpSession session = hRequest.getSession(false);

        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        @SuppressWarnings("null")
        boolean adminStatus = user.getAdministrator();

        if (adminStatus) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect("/diabetesregistry/home/");
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig the filter configuration
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("AdminFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AdminFilter()");
        }
        StringBuilder sb = new StringBuilder("AdminFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    /**
     * Logs a message
     *
     * @param msg the message
     */
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
