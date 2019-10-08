package com.ehr;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ehr.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ="/test-applicationContext.xml")
public class UserServiceTest {
	
	private Logger LOG = Logger.getLogger(UserServiceTest.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	List<User> users;
	//대상 Data생성
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("j01_134", "송영은01", "1234", Level.BASIC, 49, 0, "1150amy@naver.com", "2019/08/26")
				,new User("j02_134", "송영은02", "1234", Level.BASIC, 50, 0, "1150amy@naver.com", "2019/08/26") //등업대상 (Basic -> Silver)
				,new User("j03_134", "송영은03", "1234", Level.SILVER, 60, 29, "1150amy@naver.com", "2019/08/26")
				,new User("j04_134", "송영은04", "1234", Level.SILVER, 60, 30, "1150amy@naver.com", "2019/08/26") //등업대상 (Silver -> Gold)
				,new User("j05_134", "송영은05", "1234", Level.GOLD, 99, 99, "1150amy@naver.com", "2019/08/26")
				);
	}
	
	//최초 사용자 등록 : Level == null이면  Basic처리
	@Test
	@Ignore
	public void add() {
		//1. 전체 삭제
		//2. Level
		//3. Level == null
		//4. update
		userDao.deleteAll();
		User userExistsLevel = users.get(4); //레벨 있는 사람(gold)
		
		User userNoLevel = users.get(0); //basic
		userNoLevel.sethLevel(null);
		
		//Gold Level, Level == null 등록
		userService.add(userExistsLevel);
		//Level == null -> Level.BASIC
		userService.add(userNoLevel);
		
		//등록 데이터 GET
		User userExistsLevelRead =  userDao.get(userExistsLevel.getU_id());
		User userNoLevelRead = userDao.get(userNoLevel.getU_id());
		
		//비교
		assertThat(userExistsLevelRead.gethLevel(), is(userExistsLevel.gethLevel()));
		assertThat(userNoLevelRead.gethLevel(), is(Level.BASIC));
			
	}
	
	@Test
	public void upgradeLevels() {
		//1. 전체삭제
		//2. user등록
		//3. upgradeLevels call
		//4. 2,4사용자 upgrade대상
		
		userDao.deleteAll();
		for(User user :users) {
			userDao.add(user);
		}
		
		this.userService.upgradeLevels();
		checkUpser(users.get(0),Level.BASIC);
		checkUpser(users.get(1),Level.SILVER);
		checkUpser(users.get(2),Level.SILVER);
		checkUpser(users.get(3),Level.GOLD);
		checkUpser(users.get(4),Level.GOLD);
	}
	
	private void checkUpser(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getU_id());
		
		assertThat(userUpdate.gethLevel(), is(expectedLevel));
	}
	
	@Test
	public void serviceBean() {
		assertThat(this.userService, is(notNullValue()));
		assertThat(this.userDao, is(notNullValue()));
		LOG.info("==============================================");
		LOG.info("-userService-"+userService);
		LOG.info("-userDao-"+userDao);
		LOG.info("==============================================");
		
	}

}
