import com.nowcoder.model.testUser;
import com.nowcoder.service.testUserService;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * Created by 12274 on 2018/9/3.
 */
public class test4 {
    @Test
    public void testDB(){
        testUser user=new testUser();
        user.setName("小明");
        user.setPassword("123456");
        user.setSalt(UUID.randomUUID().toString().substring(0,6));
        user.setHeadUrl("http://www.baidu.com");
        testUserService service=new testUserService();
        service.addUser(user);
        testUser user1=service.getUserById(20);
        System.out.println(user1.getName());
        List<testUser> list=service.getAllUser();
        System.out.println(list.size());
    }
}
