package board;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

public class BoardDBBean {
	
	private static BoardDBBean instance = new BoardDBBean();
	
	private BoardDBBean() {}
	
	public static BoardDBBean getInstance() {
		return instance;
	}
	
	public Connection getConnection() throws Exception{
		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:comp/env");
		DataSource ds = (DataSource)envContext.lookup("jdbc/oracle");
		return ds.getConnection();
	}
	@SuppressWarnings("resource")
	public void insertArticle(BoardDataBean article) throws Exception{
		// db 연결하고 sql insert into~
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// to_do
		int num = article.getNum();
		int ref = article.getRef();
		int re_step = article.getReStep();
		int re_level = article.getReLevel();
		int number = 0;
		String sql = "";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select max(num) from eboard");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				number = rs.getInt(1)+1;
			} else {
				number = 1;
			}
			ref = number;
			re_step = 0;
			re_level = 0;
			sql = "insert into eboard "
					+ " (num, writer, email, subject, passwd, reg_date, ref, re_step, re_level, content, ip)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, number);
			pstmt.setString(2, article.getWriter());
			pstmt.setString(3, article.getEmail());
			pstmt.setString(4, article.getSubject());
			pstmt.setString(5, article.getPasswd());
			pstmt.setTimestamp(6, article.getRegDate());
			pstmt.setInt(7, article.getRef());
			pstmt.setInt(8, article.getReStep());
			pstmt.setInt(9, article.getReLevel());
			pstmt.setString(10, article.getContent());
			pstmt.setString(11, article.getIp());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public List<BoardDataBean> getArticles(int start, int end) throws Exception{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		List<BoardDataBean> articleList = null;
		try {
			conn = getConnection();
			sql = "select * from "
					+ " (select ROWNUM rnum, num, writer, email, subject, passwd, reg_date, readcount, ref, re_step, re_level, content, ip"
					+ " from "
					+ " (select * from eboard order by ref desc, re_step asc)"
					+ " eboard)"
					+ " where rnum >= ? and rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				articleList = new ArrayList<BoardDataBean>(end);
				do {
					BoardDataBean article = new BoardDataBean();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setEmail(rs.getString("email"));
					article.setSubject(rs.getString("subject"));
					article.setPasswd(rs.getString("passwd"));
					article.setRegDate(rs.getTimestamp("reg_date"));
					article.setReadCount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setReStep(rs.getInt("re_step"));
					article.setReLevel(rs.getInt("re_level"));
					article.setContent(rs.getString("content"));
					article.setIp(rs.getString("ip"));
					articleList.add(article);
					
				} while (rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
				try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt != null)
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn != null)
				try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return articleList;
	}
	public int getArticleCount() throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		int cnt = 0;
		try {
			conn = getConnection();
			sql = "select count(*) from eboard";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				cnt = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch (SQLException e) { e.printStackTrace();}
			if(pstmt != null) try {pstmt.close();} catch (SQLException e) { e.printStackTrace();}
			if(conn != null) try {conn.close();} catch (SQLException e) { e.printStackTrace();}
		}
		return cnt;
	}

}






























