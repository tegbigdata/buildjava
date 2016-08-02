package com.teg.dynamicClass.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	// 该url为缺省方式（省略主机跟端口）
	// 完整为：jdbc:mysql//localhost:3306/test
	public final static String FILED_SCOPE = "build_scope";
	public final static String FILED_FINALFLAG = "build_isfinal";
	public final static String FILED_STATICFLAG = "build_isstatic";
	public final static String FILED_TYPE = "build_type";
	public final static String FILED_NAME = "build_name";
	public final static String FILED_VALUE = "build_value";
	public final static String FILED_GROUPID = "build_groupid";
	public final static String FILED_ORDERID = "build_orderid";
	
	public final static String FIELD_BYTEORDERFLAG = "needByteOrder";
	public final static String FIELD_CLASSNAME = "className";
	public final static String FILED_CLASSGROUPID = "groupid";
	
	
	static String s_url = "jdbc:mysql:///test";
	static String s_name = "root";
	static String s_password = "sli";
	static Connection conn = null;
	private static JDBCUtil jdbcUtilSingle = null;

	public static JDBCUtil getInitJDBCUtil(String url, String name, String password) {
		if (jdbcUtilSingle == null) {
			// 给类加锁 防止线程并发
			synchronized (JDBCUtil.class) {
				if (jdbcUtilSingle == null) {
					jdbcUtilSingle = new JDBCUtil(url, name, password);
				}
			}
		}
		return jdbcUtilSingle;
	}

	private JDBCUtil(String url, String name, String password) {
		s_url = url;
		s_name = name;
		s_password = password;
	}

	// 通过静态代码块注册数据库驱动，保证注册只执行一次
	static {
		try {
			// 注册驱动有如下方式：
			// 1.通过驱动管理器注册驱动，但会注册两次，并且会对类产生依赖。如果该类不存在那就报错了。
			// DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			// 2.与3类似
			// System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");// 推荐使用方式
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 获得连接
	public Connection getConnection() {
		try {
			conn = DriverManager.getConnection(s_url, s_name, s_password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;

	}

	// 关闭连接
	public void closeConnection(ResultSet rs, Statement statement, Connection con) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ResultSet queryAll(String sql, Connection conn, Object... obj) throws SQLException{
		PreparedStatement psts = null;
		psts = conn.prepareStatement(sql);
		if (obj != null && obj.length > 0) {
			for (int j = 0; j < obj.length; j++) {
				psts.setObject((j + 1), obj[j]);
			}
		}
		return psts.executeQuery();

	}

	public <E> E query(String sql, Class<E> resultClass, Object... obj) throws SQLException {
		ResultSet rs = queryAll(sql, getConnection(), obj);
		try {

			if (resultClass == Map.class) {
				if (rs.next())
					return (E) getResultMap(rs);
			} else if (resultClass == List.class) {
				return (E) getResultList(rs);
			} else {
				throw new RuntimeException("" + resultClass + " 该类型目前还没有做扩展!");
			}
		} finally {
			closeConnection(rs, rs.getStatement(), rs.getStatement().getConnection());
		}
		return null;
	}

	/*
	 * 解析ResultSet 表列数据
	 */
	private Map<String, Object> getResultMap(ResultSet rs) throws SQLException {
		Map<String, Object> rawMap = new HashMap<String, Object>();
		ResultSetMetaData rsmd = rs.getMetaData(); // 表对象信息
		int count = rsmd.getColumnCount(); // 列数
		// 遍历之前需要调用 next()方法
		for (int i = 1; i <= count; i++) {
			String key = rsmd.getColumnLabel(i);
			Object value = rs.getObject(key);
			rawMap.put(key, value);
		}
		return rawMap;
	}

	/*
	 * 解析ResultSet 表数据
	 */
	private List<Map<String, Object>> getResultList(ResultSet rs) throws SQLException {
		List<Map<String, Object>> rawList = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> rawMap = getResultMap(rs);
			rawList.add(rawMap);
		}
		return rawList;
	}
}
