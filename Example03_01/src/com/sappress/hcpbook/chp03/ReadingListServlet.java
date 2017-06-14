package com.sappress.hcpbook.chp03;

import java.io.IOException;


import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ReadingListServlet
 */
@WebServlet("/ReadingListServlet")
public class ReadingListServlet extends HttpServlet {
	
	@EJB
	ReadingListManagerBeanLocal readingListManager;
	private static final long serialVersionUID = 1L;
       
	private ReadingListManagerBeanLocal getListManager(HttpSession session)
	{
		// Access/create the reading list manager EJB from the
		// session context:
		ReadingListManagerBeanLocal readingListManager = null;
		readingListManager = (ReadingListManagerBeanLocal)
				session.getAttribute("readingListManager");
		if (readingListManager == null)
		{
			try
			{
				// if the EJB hasn't been created yet. go ahead
				// and create it:
				InitialContext ctx = new InitialContext();
				readingListManager = (ReadingListManagerBeanLocal)
						ctx.lookup("java:comp/env/ejb/ReadingListManager");
				
				session.setAttribute("readingListManager",readingListManager);
				
			}
			catch (NamingException ne)
			{
				ne.printStackTrace();
			}
		}
		
		return readingListManager;
	}
    public ReadingListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Fetch the EJB list manager:
		ReadingListManagerBeanLocal readingListManager = getListManager(request.getSession());
		
		//Retrieve the target action from the request parameters:
		String action = request.getParameter("action");
		if ((action != null) && (! action.equals("Delete")))
				{
					String title = request.getParameter("title");
					readingListManager.removeTitle(title);
				}
		else
		{
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		    rd.forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Fetch the EJB list manager:
		ReadingListManagerBeanLocal readinglistManager = getListManager(request.getSession());
		
		// Add the selected title to the list:
		String title = request.getParameter("title");
		if (( title != null) && (! title.equals("")))
			readinglistManager.addTitle(title);
		
		// Reroute the user back to the main reading list form:
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request,  response);
		}
}
