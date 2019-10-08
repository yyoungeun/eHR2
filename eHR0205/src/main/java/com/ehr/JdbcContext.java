package com.ehr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * DI
 * @author sist
 *
 */
public class JdbcContext {
	private Logger LOG = Logger.getLogger(JdbcContext.class);
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public int executeSql(String query,User user) throws SQLException{
		int flag  = 0;
		//익명클래스(annoymous innser class)	이름을 갖지 않는 클래스
		//new 인터페이스이름(){클래스본문};
		flag = workWithStatementStrategy(
				new StatementStrategy() {
					public PreparedStatement makePrepardStatement(Connection c) throws SQLException {		
						PreparedStatement ps = 
								c.prepareStatement(query);
						ps.setString(1, user.getU_id());
						return ps;
					}
				}
			);
		return flag;
	}

	public int workWithStatementStrategy(StatementStrategy st)
			  throws SQLException{
				//----------------------------------------
				//01.DB 연결
				//----------------------------------------		
				Connection  c = null;
				PreparedStatement ps = null;
				int flag = 0;
				
				try {
					c = dataSource.getConnection();
					LOG.debug("=============================");
					LOG.debug("01. Connection="+c);
					LOG.debug("=============================");		
					
					//========================================
					//=변하는 부분 메소드로 추출                                            =
					//========================================
					ps = st.makePrepardStatement(c);
					
					//----------------------------------------
					//04.Run sql
					//----------------------------------------	
					flag = ps.executeUpdate();
					LOG.debug("=============================");
					LOG.debug("04. Run flag=\n"+flag);
					LOG.debug("=============================");		
				}catch(SQLException s) {
					LOG.debug("=============================");
					LOG.debug("04. SQLException=\n"+s.getMessage());
					LOG.debug("=============================");				
				}finally {
					//----------------------------------------
					//05.자원반납
					//----------------------------------------	
					if(null !=ps) {
						try {
							ps.close();
						}catch(SQLException s) {
							
						}
					}
					
					if(null !=c) {
						try {
							c.close();
						}catch(SQLException s) {
							
						}
					}			
				}

				
				return flag;		
			}
}
