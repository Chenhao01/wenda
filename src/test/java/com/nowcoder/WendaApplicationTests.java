package com.nowcoder;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.dao.testUserDao;
import com.nowcoder.model.Question;
import com.nowcoder.model.testUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
//@WebAppConfiguration
public class WendaApplicationTests {
	@Autowired
	QuestionDAO questionDAO;
	@Autowired
	testUserDao UserDao;

	@Test
	public void contextLoads() {
		/*List<Question> questions=questionDAO.selectLatestQuestions(0,0,2);
		for(Question question:questions){
			System.out.println(question.getTitle());
			System.out.println(question.getContent());
		}*/
		List<testUser> list=UserDao.selectAllUser();
		testUser user=new testUser();
		user.setName("小明");
		user.setPassword("123456");
		user.setSalt(UUID.randomUUID().toString().substring(0,6));
		user.setHeadUrl("http://www.baidu.com");
		UserDao.addUser(user);
	}

}
