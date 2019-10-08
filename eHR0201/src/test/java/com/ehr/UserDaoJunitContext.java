package com.ehr;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")

public class UserDaoJunitContext {
	
	Logger LOG = Logger.getLogger(this.getClass());
	@Autowired
	ApplicationContext context;
	
	//---------------------------
	//@Before setUp()
	//---------------------------
	@Before 
	public void setUp() {
		LOG.debug("context: " + context);
		LOG.debug("this: " + this);
		LOG.debug("01 setUp()");
	}
	//---------------------------
	//@Test
	//---------------------------
	@Test
	public void count() {
		LOG.debug("02 count()");
	}
	//---------------------------
	//@Test
	//---------------------------
	@Test
	public void addAndGet() {
		LOG.debug("03 addAndGet()");
	}
	//---------------------------
	//@After tearDown()
	//---------------------------
	@After
	public void tearDown() {
		LOG.debug("04 tearDown()");
	}
}
