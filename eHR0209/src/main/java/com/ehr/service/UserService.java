package com.ehr.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.ehr.Level;
import com.ehr.User;
import com.ehr.UserDao;

public class UserService {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private UserDao userDao;
	public static final int MIN_LOGINCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	public void setUserDao(UserDao userDao) { //주입을 위한 setter
		this.userDao = userDao;
	}
	
	public void add(User user) {
		if(null == user.gethLevel())user.sethLevel(Level.BASIC);
		
		userDao.add(user);
	}
	
	//level upgrade
	//1. 전체 사용자를 조회
	//2. 대상자를 선별
	//	2.1 BASIC사용자,  로그인 cnt 50이상이면: Basic -> SILVER
	//	2.1 SILVER사용자, 추천 cnt 30이상이면: SILVER -> GOLD
	//	2.3 GOLD 대상 아님.
	//3. 대상자를 업그레이드 레벨선정 및 upgrade
	
//	public void upgradeLevels() {
//		int  upCnt = 0;
//		//1. 전체 사용자를 조회
//		List<User> users = userDao.getAll();
//		
//		for(User user :users) {
//			Boolean changed = null;
//			//BASIC -> SILVER
//			if(user.gethLevel()==Level.BASIC && user.getLogin()>= 50) {
//				user.sethLevel(Level.SILVER);
//				changed = true;
//			
//			//SILVER -> GOLD
//			}else if(user.gethLevel()==Level.SILVER && user.getRecommend() >= 30) {
//				user.sethLevel(Level.GOLD);
//				changed = true;
//				
//			}else if(user.gethLevel() == Level.GOLD) {
//				changed = false;
//			}else {
//				changed = false;
//			}
//			if(changed == true) {
//				userDao.update(user);
//				upCnt++;
//			}
//		}//for
//		
//		LOG.debug("--------------------------------------------");
//		LOG.debug("===upCnt =" + upCnt);
//		LOG.debug("--------------------------------------------");
//	}
	
	protected void upgradeLevel(User user) {
//		if(user.gethLevel()==Level.BASIC) {
//			user.sethLevel(Level.SILVER);
//		}else if(user.gethLevel()==Level.SILVER) {
//			user.sethLevel(Level.GOLD);
//		}
		user.upgradeLevel();
		userDao.update(user);
	}
	
	//두가지 기능 분리(upgrade대상, upCnt)
	//level upgrade
	//1. 전체 사용자를 조회
	//2. 대상자를 선별
	//	2.1 BASIC사용자,  로그인 cnt 50이상이면: Basic -> SILVER
	//	2.1 SILVER사용자, 추천 cnt 30이상이면: SILVER -> GOLD
	//	2.3 GOLD 대상 아님.
	//3. 대상자를 업그레이드 레벨선정 및 upgrade
	
	public void upgradeLevels() throws SQLException {
		//1. 트랜잭션 동기화 관리자를 초기화
		
		//2. 트랜잭션: Connection.setAutoCommit(false)
		
		//3. Connection.commit();
		
		//3. Connection.rollback();
		
		//4. 자원반납
		
		int upCnt = 0;
		//트랜잭션 동기화: 초기화
		TransactionSynchronizationManager.initSynchronization();
		Connection c = DataSourceUtils.getConnection(dataSource);

		c.setAutoCommit(false);//트랜잭션 제어는 개발자
		List<User> users = userDao.getAll(); //전체사용자 꺼내기
		
		try {
		
		for(User user : users) {
			if(canUpgradeLevel(user) == true) {
				upgradeLevel(user);
				upCnt++;
			}
			
		}//for
		
			c.commit();//정상이면 Commit
		}catch(Exception e) {
			LOG.info("==================================================");
			LOG.info("=Exception= " + e.toString());
			LOG.info("=rollback c= " + c);
			LOG.info("==================================================");
			c.rollback(); //실패면 rollback
			throw e;
		}finally {
			//자원반납
			DataSourceUtils.releaseConnection(c, dataSource);
			//동기화 종료
			TransactionSynchronizationManager.unbindResource(dataSource);
			TransactionSynchronizationManager.clearSynchronization();//동기화
		}
		LOG.info("==================================================");
		LOG.info("=upCnt= " + upCnt);
		LOG.info("==================================================");
	}
	
	//update대상 여부 파악: true
	private boolean canUpgradeLevel(User user) {
		Level currLevel = user.gethLevel();
		
		switch(currLevel) {
			case BASIC:
				return (user.getLogin() >= MIN_LOGINCOUNT_FOR_SILVER);
			case SILVER:
				return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
			case GOLD:
				return false;
			default:
				throw new IllegalArgumentException("Unknown Level: "+ currLevel); //예외
			}
	}
	
}
