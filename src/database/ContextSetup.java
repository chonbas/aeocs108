package database;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ContextSetup
 *
 */
@WebListener
public class ContextSetup implements ServletContextListener {
	
    /**
     * Default constructor. 
     */
    public ContextSetup() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent e)  { 
    	DB_Interface db = (DB_Interface)e.getServletContext().getAttribute("db");
    	db.closeConnection();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent e)  { 
    	DB_Interface db = null;
		try {
			db = new DB_Interface();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
         e.getServletContext().setAttribute("db", db); 
         
         
    }
	
}
