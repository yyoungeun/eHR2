package com.ehr;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoJunitFinal {
	
	private Logger LOG = Logger.getLogger(UserDaoJunitFinal.class);
	
	@Autowired
	ApplicationContext context;

	private UserDao dao;
	private User user01;
	private User user02;
	private User user03;
	
	@Before
	public void setUp() {
		LOG.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		LOG.debug("setUp()");
		LOG.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		user01 = new User("j01_134","송영은01","1234");
		user02 = new User("j02_134","송영은02","1234");
		user03 = new User("j03_134","송영은03","1234");
		
		//AbstractApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		dao = context.getBean("userDao",UserDao.class);
		LOG.debug("=================================");
		LOG.debug("=01 context=" + context);
		LOG.debug("=01 dao=" + dao);
		LOG.debug("=================================");
	}
	
	@Test
	public void getAll() throws SQLException {
		//------------------------------------------------------------------
		//삭제
		//------------------------------------------------------------------
		//dao.deleteUser(user01);
		//dao.deleteUser(user02);
		//dao.deleteUser(user03);
		
		List<User> list = dao.getAll();
		for(User user: list) {
			LOG.debug("user: " + user);
		}
	}
	
		@After
		public void tearDown() {
			LOG.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			LOG.debug("99 tearDown()");
			LOG.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		}
		
	  @Test(expected = EmptyResultDataAccessException.class) 
	  @Ignore
	  public void getFailure() throws ClassNotFoundException, SQLException {
		  	
			User user01 = new User("j01_134","송영은01","1234");
			User user02 = new User("j02_134","송영은02","1234");
			User user03 = new User("j03_134","송영은03","1234");
			
			//------------------------------------------------------------------
			//전체 삭제
			//------------------------------------------------------------------
			dao.deleteUser(user01);
			dao.deleteUser(user02);
			dao.deleteUser(user03);
			assertThat(dao.count("_134"), is(0));
			
			dao.get("unknownUserId");
	  }
	 
	@Test
	@Ignore
	public void count() throws ClassNotFoundException, SQLException {
		
		//------------------------------------------------------------------
		//전체 삭제
		//------------------------------------------------------------------
		dao.deleteUser(user01);
		dao.deleteUser(user02);
		dao.deleteUser(user03);
		assertThat(dao.count("_134"), is(0));
		
		//------------------------------------------------------------------
		//1건 추가
		//------------------------------------------------------------------
		dao.add(user01);
		assertThat(dao.count("_134"), is(1));
		//------------------------------------------------------------------
		//1건 추가
		//------------------------------------------------------------------
		dao.add(user02);
		assertThat(dao.count("_134"), is(2));
		//------------------------------------------------------------------
		//1건 추가
		//------------------------------------------------------------------
		dao.add(user03);
		assertThat(dao.count("_134"), is(3));
		
		//------------------------------------------------------------------
		//count :3
		//------------------------------------------------------------------
		
	}
	@Test(timeout = 5000) //1. JUnit에게 테스트용 메소드임을 알려줌
	@Ignore
	public void addandGet() {//2. method : public으로 선언!
		
		// j01_ip: j01_134

				  try {
					  dao.deleteUser(user01);
					  dao.deleteUser(user02);
					  dao.deleteUser(user03);
					  assertThat(dao.count("_134"), is(0));
					  
					  LOG.debug("================================="); 
					  LOG.debug("=01 단건등록=");
					  LOG.debug("=================================");
				  
					  int flag = dao.add(user01);
					  flag = dao.add(user02);
					  flag = dao.add(user03);
					  assertThat(dao.count("_134"), is(3));
					  LOG.debug("=================================");
					  LOG.debug("=01.01 add flag=" + flag);
					  LOG.debug("=================================");
				  
					  assertThat(flag, is(1));
					  LOG.debug("flag= " + flag);
				  
					  LOG.debug("================================="); 
					  LOG.debug("=02 단건조회=");
					  LOG.debug("=================================");
			  
					  User userOne = dao.get(user01.getU_id());
					  
					  assertThat(userOne.getU_id(), is(user01.getU_id()));
					  assertThat(userOne.getName(), is(user01.getName()));
					  assertThat(userOne.getPasswd(), is(user01.getPasswd()));
					  
				  }catch (ClassNotFoundException e) { // TODO Auto-generated catch block
				  e.printStackTrace(); } catch (SQLException e) { // TODO Auto-generated catch block
				  e.printStackTrace(); 
				  }
			}
	}
