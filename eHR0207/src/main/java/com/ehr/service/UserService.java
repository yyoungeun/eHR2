package com.ehr.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.ehr.Level;
import com.ehr.User;
import com.ehr.UserDao;

public class UserService {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	
	private UserDao userDao;


	public void setUserDao(UserDao userDao) {
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
	
	private void upgradeLevel(User user) {
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
	
	public void upgradeLevels() {
		int upCnt = 0;
		List<User> users = userDao.getAll(); //전체사용자 꺼내기
		
		for(User user : users) {
			if(canUpgradeLevel(user) == true) {
				upgradeLevel(user);
				upCnt++;
			}
			
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
				return (user.getLogin() >= 50);
			case SILVER:
				return (user.getRecommend() >= 30);
			case GOLD:
				return false;
			default:
				throw new IllegalArgumentException("Unknown Level: "+ currLevel); //예외
			}
	}
	
}
