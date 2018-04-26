package org.jboss.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VersionReleaseServlet extends HttpServlet {

    private int count;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doIt(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doIt(request, response);
    }

    private void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DONE");

        int counter = getCount(request);

        String version = getServletContext().getInitParameter("VERSION");
        String context = getServletContext().getInitParameter("CONTEXTNAME");

        String message = "VERSION IS: " + version + " - You have visited this context [" + context + "] [" + counter + "] times.";

        System.out.println(message);

        response.getWriter().print(message);
    }

    public int getCount(HttpServletRequest request) {
        Object counterObj = request.getSession().getAttribute("counter");
        int counter = 0;
        if (counterObj != null && counterObj instanceof Integer) {
            counter = ((Integer) counterObj).intValue();
        }
        counter++;
        request.getSession().setAttribute("counter", new Integer(counter));
        return counter;
    }
}
