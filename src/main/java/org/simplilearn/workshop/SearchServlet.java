package org.simplilearn.workshop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.simplilearn.workshop.util.StringUtil;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String keyword = "";

	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("JDBC driver loaded");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		sendPageHeader(response);
		sendSearchForm(response);
		sendPageFooter(response);
	}

	private void sendSearchForm(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.println("<BR><H2>Search Form</H2>");
		out.println("<BR> Please enter the Product ID for searching");
		out.println("<BR>");
		out.println("<BR><FORM METHOD=POST>");
		out.println("ID : <INPUT TYPE=TEXT NAME=keyword");
		out.println(" VALUE=\"" + StringUtil.encodeHtmlTag(keyword) +"\"");
		out.println(">");
		out.println("<INPUT TYPE=SUBMIT>");
		out.println("</FORM>");
		out.println("<BR>");
		out.println("<BR>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		keyword = request.getParameter("keyword");
		sendPageHeader(response);
		sendSearchForm(response);
		sendSearchResult(response);
		sendPageFooter(response);
	}

	private void sendPageHeader(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE> Displaying Selected Record(s) </TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<CENTER>");
	}

	void sendSearchResult(HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			String url = "jdbc:mysql://localhost:3307/products";
			String user = "root";
			String pass = "K9039680300p";

			// step 2: obtain a connection
			Connection con = DriverManager.getConnection(url, user, pass);
			System.out.println("got connection");

			

			String sql = "SELECT Product_id, Product_name, Product_category, Product_rating FROM product_table WHERE Product_id="+keyword;
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if(rs.next())
			{
			while (rs.next()) {
				out.println("<TABLE>");
				out.println("<TR>");
				out.println("<TH>Product ID </TH>");
				out.println("<TH>Product Name </TH>");
				out.println("<TH>Product Category</TH>");
				out.println("<TH>Product Rating</TH>");
				out.println("<TH></TH>");
				out.println("<TH></TH>");
				out.println("</TR>");
				out.println("<CENTER>");
				out.println("<TR>");
				out.println("<TD>" + StringUtil.encodeHtmlTag(rs.getString(1)) + "</TD>");
				out.println("<TD>" + StringUtil.encodeHtmlTag(rs.getString(2)) + "</TD>");
				out.println("<TD>" + StringUtil.encodeHtmlTag(rs.getString(3)) + "</TD>");
				out.println("<TD>" + StringUtil.encodeHtmlTag(rs.getString(4)) + "</TD>");
				out.println("</TR>");
				out.println("</CENTER>");
			}
			}
			else
			{
				out.println("Entered ID is not in the database ,Please enter a valid Product ID");
			}
			s.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("</TABLE>");
	}
	
	private void sendPageFooter(HttpServletResponse response) throws IOException {
		PrintWriter out  = response.getWriter();
		out.println("</CENTER>");
		out.println("</BODY>");
		out.println("</HTML>");
	}
}