// T. J. Flesher
// Assignment 5.1
// CIS404 / Java
// Bellevue University
// Create a new Web application titled <yourname>Week5. In this application you will also create a virtual directory so that you do not have to use the word servlet as part of your form post URL. 
// You will then be able to run your Web application servlet by using a URL similar to the following: http://localhost:7070/<yourname>Week5/<yourname>FormPost2. 
// To create your virtual directory you may start by modifying the web.xml attached to this assignment. 
// Next, create a Servlet that displays a form when the doGet method is invoked. 
// The form will contain a post action that directs the form post back to the same servlet, which in the doPost method will save the form data to a database. 
// Use your Oracle account to make the DB connection. After the form data has been saved to the database, respond back with a query from the database displaying all the current records contained in the database,
// in an appealing format. The form must contain a minimum of three input fields. 
// The grade for this assignment will be based both on the functionality of the servlet and the appearance of the form post results. 
// Name your servlet <yourName>FormPost2 and name the application <yourname>Week5. Create a Web archive file and attach to this assignment. 

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;


public class TFlesherFormPost2 extends HttpServlet{
		
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	String tableName = "GRADES_WEEK5";
		
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
				//content type
	response.setContentType("text/html");
	
	PrintWriter out = response.getWriter();
	printHeader(out);
	out.println("<h4>doGet: Database events</h4>");
	CreateTable(out);
	out.println("<hr/>");
	printForm(out);
	printFooter(out);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			final String name = request.getParameter("studentName");
			final String grade = request.getParameter("selectGrade");
			final String pf = request.getParameter("rdoPassFail");
			
			PrintWriter out = response.getWriter();
			printHeader(out);
			printForm(out);
			out.println("<br/><hr/><h4>doPost: Database Insert events</h4>");		
			InsertData(out,name,grade,pf);
			out.println("<hr/>");
			//printForm(out);
			//out.println("<br/><hr/>");
			out.println("<h4>doPost: Database Display events</h4>");
			DisplayData(out);
			printFooter(out);
			
		}
	
	public void printHeader(PrintWriter out){
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " +
         "transitional//en\">\n";
		out.println(docType);
		out.println("<html>");
		out.println("<head> <meta charset='UTF-8'>");
		out.println("<title>");
		out.println("Assignment 5.1 Form & Databases");
		out.println("</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div>");
	}
	
	public void printFooter(PrintWriter out){
	out.println("</div>");
	out.println("</body>");
	out.println("</html>");
	}
	
	public void printForm(PrintWriter out){
		out.println("<form method='post' action='TFlesherFormPost2'>");
		out.println("<h2>The Grade Book!</h2>");
		out.println("<table border='0'>" +
					"<tr>" +
						"<td>" +
							"<label>Name of Student:</label> " +
						"</td>" +
						"<td>" +
							"<input type='text' name='studentName' size='35' maxlength='35'/>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td>" +
							"<label>Select Grade:</label>" +
						"</td>" +
						"<td>" +
							"<select name='selectGrade'>" +
							"<option selected='selected' value='A'>A</option>" +
							"<option value='B'>B</option>" +
							"<option value='C'>C</option>" +
							"<option value='D'>D</option>" +
							"<option value='F'>F</option>" +
							"</select>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td>" +
							"<label>Did the student Pass?</label>" +
						"</td>" +
						"<td>" +
							"<input type='radio' name='rdoPassFail' value='Pass' checked='checked'/><label>PASSED</label>" +
							"<input type='radio' name='rdoPassFail' value='Fail' /><label>FAILED</label>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td colspan='2' valign='bottom' align='right' style='padding-right:10px;'>" +
							"<input type='submit' value='Submit' />" +
							"<input type='reset' value='Reset' />" +
						"</td>" +
					"</tr>" +
				"</table>");
		out.println("</form>");
	}
	
	public void CreateTable(PrintWriter out){
		//creating connection
		try{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			//Connect to the URL
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student1", "pass");
			//statement
			stmt = con.createStatement();
		} catch(SQLException e){
			out.println("<h2>Database connection error! </h2>" + e.getMessage());
			return;
		}
		//dropping table
		try{
			stmt.executeUpdate("DROP TABLE " + tableName);
			out.println("<i>Table dropped</i><br/>");
		} catch (SQLException s){
			out.println("<i>Table not dropped, not found!</i><br/>");
		}
		//create table
		try{
			stmt.executeUpdate("CREATE TABLE " + tableName + " (NAME VARCHAR(35) NOT NULL, GRADE CHAR(1) NOT NULL, PASSFAIL CHAR(8) NOT NULL)");
			out.println("<i>Table created: " + tableName +"</i><br/>");
		} catch (SQLException s){
			out.println("<i>Table: " + tableName + " failed to create!</i><br/>");
		}
		//close
		try {
			stmt.close();
			con.close();
			out.println("<i>Database connection is closed</i><br/>");
		} catch (SQLException s) {
			out.println("<i>Connectoin is not closed<i><br/>");
		}
	}
	
	public void InsertData(PrintWriter out, String sName, String sGrade, String pfStatus){
		try{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			//Connect to the URL
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student1", "pass");
			//statement
			stmt = con.createStatement();
		} catch(SQLException e){
			out.println("<h3>InsertData: Database connection errored --> </h3>" + e.getMessage());
			return;
		}
		try {
			stmt.executeUpdate("INSERT INTO " + tableName + "(NAME,GRADE,PASSFAIL) VALUES('"+sName+"','"+sGrade+"','"+pfStatus+"')");
			stmt.executeUpdate("COMMIT");	
			out.println("<i>Insert statement committed.</i><br/>");
		} catch (SQLException s){
			out.println("<i>Failed to insert data<i><br/>");
		}
		try{
			stmt.close();
			con.close();
			out.println("<i>Close database connection</i><br/>");
		}catch (SQLException s){
			out.println("<i>Failed to close database connection</i><br/>");
		}
	}
	
	public void DisplayData(PrintWriter out){
		
		try{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			//Connect to the URL
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student1", "pass");
			//statement
			stmt = con.createStatement();
		} catch(SQLException e){
			out.println("<h3>DisplayData: Database connection errored --> </h3>" + e.getMessage());
			return;
		}
		try {
			rs = stmt.executeQuery("SELECT NAME,GRADE,PASSFAIL FROM " + tableName );
			out.println("<i>SELECT statement executed..</i><br/><br/>");
		} catch (SQLException s){
			out.println("<i>Failed to select data<i><br/>");
		}
		// try{
			// stmt.close();
			// con.close();
			// out.println("<i>Close database connection</i><br/>");
		// }catch (SQLException s){
			// out.println("<i>Failed to close database connection</i><br/>");
		// }
		try{
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNumber = rsmd.getColumnCount();
			int counter = 1;
			while(rs.next()){
				out.println("Record: "+counter+"<br/>");
				for (int i = 1; i <= colNumber; i++){
					//if(i > 1) out.print(", ");
					String colValue = rs.getString(i);
					out.print(rsmd.getColumnName(i)  + ": " + colValue +"<br/>");
				}
				out.println("<br/>");
				counter++;
			}
			stmt.close();
			con.close();
		}
		catch (SQLException s){
			out.println("<i>Failed to close database connection</i><br/>");
		}
	}
}
