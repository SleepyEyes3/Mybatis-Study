import com.jxy.dao.UserDao;
import com.jxy.pojo.User;
import com.jxy.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1. 增删改一定要加事物commit才能完成提交！
 *
 *
 *
 * */
public class Test1 {

    static Logger logger = Logger.getLogger(Test1.class);
    @Test
    public void getUserListByResultMapTest(){
        logger.info("--->info");
        logger.debug("--->debug");
        logger.error("--->error");

        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserListByResultMap(1);
        for(User user:userList){
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void getUserListByLimit(){

        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("startIndex",2);
        map.put("pageSize",2);
        List<User> userList = mapper.getUserLimit(map);
        for(User user:userList){
            System.out.println(user);
        }
        sqlSession.close();
    }
}
