package com.transn.dao.base.jpa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.sql.DataSource;

/*
 * 作者:
 * 功能:执行任何对象的增,删,改,查;执行任何带参数与不带参数,有返回结果和无返回结果的SQL语句和存储过程
 * 时间:2011年12月30日
 */
public class DBAgent {
	// 是否显示执行的sql语句
	private boolean showSql = false;

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	public boolean isShowSql() {
		return showSql;
	}

	// 使用数据连接池依赖注入
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// 输出sql语句
	private void printSql(String sql) {
		if (this.showSql) {
			System.out.println(sql);
		}
	}

	// 得到连接对象
	public Connection getConnection() {
		Connection con = null;
		try {
			con = this.dataSource.getConnection();
		} catch (Exception e) {
			System.out.println("获取连接失败...");
			e.printStackTrace();
		}
		return con;
	}

	/*** 执行无参数有结果集的sql语句 */
	public Map[] runSelect(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		Result result = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(sql);
			printSql(sql);
			res = ps.executeQuery();
			result = ResultSupport.toResult(res);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				res.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result.getRows();
	}

	/** 执行有参数有结果集的sql语句�в��� */
	public Map[] runSelect(String sql, Object[] params) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		Result result = null;
		try {

			con = getConnection();
			ps = con.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			printSql(sql);
			res = ps.executeQuery();
			result = ResultSupport.toResult(res);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				res.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result.getRows();
	}

	/** 执行无参数没有结果集的sql语句 */
	public boolean runUpdate(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(sql);
			printSql(sql);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/** 执行有参数无结果集的sql语句 */
	public boolean runUpdate(String sql, Object[] params) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			printSql(sql);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** ************************以下执行存储过程*********************** */

	/** 执行无参数无结果集的存储过程 */
	public boolean runUpdateProc(String procName) {
		Connection con = null;
		CallableStatement cs = null;
		try {
			con = getConnection();
			String proc = "{ call " + procName + "()}";
			cs = con.prepareCall(proc);
			cs.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				cs.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 执行有参数无结果集的存储过程 */
	public boolean runUpdateProc(String procName, Object[] params) {
		Connection con = null;
		CallableStatement cs = null;
		try {
			con = getConnection();
			String proc = "{ call " + procName + "(";
			for (int i = 0; i < params.length; i++) {
				proc += "?,";
			}
			proc = proc.substring(0, proc.length() - 1) + ")}";
			cs = con.prepareCall(proc);
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null) {
					cs.setObject(i + 1, params[i]);
				} else {
					cs.registerOutParameter(i + 1, 12);
				}
			}
			cs.executeUpdate();
			for (int i = 0; i < params.length; i++) {
				if (params[i] == null) {
					params[i] = cs.getObject(i + 1);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				cs.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 执行无参数有结果集的存储过程 */
	public Map[] runSelectProc(String procName) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String proc = "{ call " + procName + "(?)}";
			cs = con.prepareCall(proc);
			cs.registerOutParameter(1, -10);
			cs.executeQuery();
			rs = (ResultSet) cs.getObject(1);
			Result result = ResultSupport.toResult(rs);
			return result.getRows();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
				cs.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 执行有参数有结果集的存储过程 */
	public Map[] runSelectProc(String procName, Object[] params) {
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String proc = "{ call " + procName + "(";
			for (int i = 0; i < params.length; i++) {
				proc += "?,";
			}
			proc += "?)}";
			cs = con.prepareCall(proc);
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null) {
					cs.setObject(i + 1, params[i]);
				} else {
					cs.registerOutParameter(i + 1, 12);
				}
			}

			cs.registerOutParameter(params.length + 1, -10);
			cs.executeQuery();
			for (int i = 0; i < params.length; i++) {
				if (params[i] == null) {
					params[i] = cs.getObject(i + 1);
				}
			}
			rs = (ResultSet) cs.getObject(params.length + 1);
			Result result = ResultSupport.toResult(rs);
			return result.getRows();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
				cs.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 分页查询,有参数 */
	public Map[] pageSql(String sql, Object[] params, int pageIndex,
			int pageSize) {
		// 对mssql的优化
		sql = sql + " LIMIT " + (pageIndex -1) + "," + pageSize;
		printSql(sql);
		return runSelect(sql, params);
	}

	/** 分页查询,无参数 */
	public Map[] pageSql(String sql, int pageIndex, int pageSize) {
		// 拼凑分页的SQL语句
		// 对mssql的优化
		sql = sql + " LIMIT " + (pageIndex -1)*pageSize + "," + pageSize;
		printSql(sql);
		return runSelect(sql);
	}

	/** 查询记录条数,该sql语句中不能带count统计函数 */
	public int getRecordCount(String sql, Object[] params) {
		String temp_sql = String.format("select count(*) n from (%s)", sql);
		Map[] rows = runSelect(temp_sql, params);
		return Integer.parseInt(rows[0].get("n").toString());
	}

	/** 查询记录条数,该sql语句中不能带count统计函数 */
	public int getRecordCount(String sql) {
		String temp_sql = String.format("select count(*) n from (%s) as tmp", sql);
		Map[] rows = runSelect(temp_sql);
		return Integer.parseInt(rows[0].get("n").toString());
	}

	/** 通过查询返回唯一的结果,有参数,例如执行一些聚集函数:sum,max,count等,只需指定条件即可 */
	public Object uniqueResultFromSingleTable(String sql, Object[] params) {
		try {
			Map[] rows = runSelect(sql, params);
			if (rows.length != 1) {
				throw new Exception();
			}
			Object[] valueArr = rows[0].values().toArray();
			if (valueArr.length != 1) {
				throw new Exception();
			}
			return rows[0].values().toArray()[0];
		} catch (Exception e) {
			System.out.println("结果不唯一");
			return null;
		}
	}

	/** 通过查询返回唯一的结果,无参数,例如执行一些聚集函数:sum,max等,只需指定条件即可 */
	public Object uniqueResultFromSingleTable(String sql) {
		try {
			Map[] rows = runSelect(sql);
			if (rows.length != 1) {
				throw new Exception();
			}
			Object[] valueArr = rows[0].values().toArray();
			if (valueArr.length != 1) {
				throw new Exception();
			}
			return rows[0].values().toArray()[0];
		} catch (Exception e) {
			System.out.println("结果不唯一");
			return null;
		}
	}

	// 批量插入
	public boolean bulkInsert(String insertSql, List<Object[]> paramsList) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(insertSql);
			for (int j = 0; j < paramsList.size(); j++) {
				for (int i = 0; i < paramsList.get(j).length; i++) {
					ps.setObject(i + 1, paramsList.get(j)[i]);
				}
				ps.addBatch();
				printSql(insertSql);
				// 分批提交，避免出现内存不足
				if (j % 1000 == 0) {
					ps.executeBatch();
				}
			}
			ps.executeBatch();
			con.commit();
			return true;
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ex) {
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
