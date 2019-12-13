package chapter9;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SelectServlet1
 */
@WebServlet("/SqlInjection")
public class SqlInjection extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		String name = request.getParameter("name");
		String sqlStr = "SELECT * FROM SUBJECT WHERE SUBJECTID = '"+name+"'";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sampledb?serverTimezone=JST"
					,"root","root");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlStr);
			
			while(rs.next()) {
				String s = "<td>" + rs.getInt("SUBJECTID") + "</td>"
				+ "<td>" + rs.getString("SUBJECTNAME") + "</td>";
				list.add(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(rs != null) {
				try {
					rs.close();
				}catch(SQLException sqlEx) {
				}
			}
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException sqlEx) {
				}
			}
		}
		
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset\"UTF-8\">");
		out.println("<title>SQLインジェクション</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h2>結果</h2>");		
		out.println("<table border = \"1\">");
		out.println("<tr>");
		out.println("<th>科目ID</th>");
		out.println("<th>科目名</th>");
		out.println("</tr>");
		for(String str : list) {
			out.println("<tr>" + str + "</tr>");
		}
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");

	}
}
