import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.servlet.annotation.*;

//@WebServlet("/TFlesherFormPost") 

public class TFlesherFormPost extends HttpServlet{
	
	private final static String filePath = "c:/temp/TFlesherweek4.dat";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		//content type
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		printHeader(out);
		printForm(out);
		printFooter(out);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		final String fname = request.getParameter("fname");
		final String lname = request.getParameter("lname");
		final String email = request.getParameter("email");
		final String accept = request.getParameter("agreement");
		WriteToFile(fname,lname,email,accept);
		
		PrintWriter out = response.getWriter();
		printHeader(out);
		out.println("<h2>Output results:</h2><hr/>");
		ReadFile(out);
		printFooter(out);
		
	}
	
	public void WriteToFile(String fname, String lname, String email, String agreement) {
		//String fname, lname, email, agreement;
		try{
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			long fileSize = file.length();
			file.seek(fileSize);
			file.writeUTF(fname);
			for(int i=0; i < 40-fname.length(); i++){
				file.writeByte(40);
			}
			file.writeUTF(lname);
			for(int i = 0; i < 40 - lname.length(); i++){
				file.writeByte(40);
			}
			file.writeUTF(email);
			for(int i = 0; i < 50 - email.length(); i++){
				file.writeByte(50);
			}
			file.writeUTF(agreement);
			for(int i = 0; i < 6 - agreement.length(); i++){
				file.writeByte(6);
			}
			file.close();
		} catch (IOException e){
			e.getStackTrace();
		}
		
	}
	
	public void ReadFile(PrintWriter out) {
		final int RECORD = 136;  //This value is total length of input fields
		String fname = null, lname = null, email = null, agreement = null;
		try {
			RandomAccessFile file = new RandomAccessFile(filePath, "r");
			long fileSize = file.length();
			file.seek(0);
			long NumRecords = fileSize / RECORD;
			int counter = 1;
			
			for(int j = 0; j < NumRecords; j++){  //loops through dat file for individual records
				fname = file.readUTF();
				for(int x = 0; x < 40 - fname.length(); x++){
					file.readByte();
				}
				lname = file.readUTF();
				for(int x = 0; x < 40 - lname.length(); x++){
					file.readByte();
				}
				email = file.readUTF();
				for(int x = 0; x < 50 - email.length(); x++){
					file.readByte();
				}
				agreement = file.readUTF();
				for(int x = 0; x < 6 - agreement.length(); x++){
					file.readByte();
				}
				out.println("<h4>Record # " + counter + "</h4>");
				out.println("First name entered: ");
				out.println(fname);
				out.println("<br/>Last name entered: ");
				out.println(lname);
				out.println("<br/>Email: ");
				out.println(email);
				out.println("<br/>Terms Accepted: ");
				out.println(agreement);
				counter++;
			}
		} catch (IOException e){
			e.getStackTrace();
		}
	}
	
	public void printHeader(PrintWriter out){
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " +
         "transitional//en\">\n";
		out.println(docType);
		out.println("<html>");
		out.println("<head> <meta charset='UTF-8'>");
		out.println("<title>");
		out.println("Assignment 4.1 Servlet and Form");
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
		out.println("<form method='post' action='TFlesherFormPost'>");
		out.println("<h2>Welcome to Buy N Large Records</h2>");
		out.println("<p>This page is used to sign up for monthly sets of Records for $9.99 a month!</p>");
		out.println("<table border='0'>" +
					"<tr>" +
						"<td>" +
							"<label>Enter your first name:</label> " +
						"</td>" +
						"<td>" +
							"<input type='text' name='fname' size='40' maxlength='40'/>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td>" +
							"<label>Enter your last name:</label>" +
						"</td>" +
						"<td>" +
							"<input type='text' name='lname' size='40' maxlength='40'/>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td>" +
							"<label>Enter your email address:</label>" +
						"</td>" +
						"<td>" +
							"<input type='email' name='email' size='50' maxlength='50'/>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td colspan='2' width='75px;' style='color:red;font-style:italic;'>" +
							"Legal notice:  By accepting the terms and policies you will fullfil your obligation of purchase every month or be penalized per agreement." +
						"</td>" +
					"</tr>"+
					"<tr>"+
						"<td colspan='2' align='right' style='padding-right:48px;' >"+
							"<input type='checkbox' name='agreement' value='Accept' required/> Accept "+
						"</td>"+
					"</tr>"+
					"<tr>" +
						"<td colspan='2' valign='bottom' align='right' style='padding-right:10px;'>" +
							"<input type='submit' value='Submit' />" +
							"<input type='reset' value='Reset' />" +
						"</td>" +
					"</tr>" +
				"</table>");
		out.println("</form>");
	}
	
}