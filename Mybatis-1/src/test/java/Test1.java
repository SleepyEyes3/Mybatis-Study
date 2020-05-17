import com.jxy.dao.UserDao;
import com.jxy.pojo.User;
import com.jxy.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
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
    @Test
    public void listTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserList();
        for(User user:userList){
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void selectTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserDao mapper = sqlSession.getMapper(UserDao.class);
        User userById = mapper.getUserById(1);
        System.out.println(userById);
        sqlSession.close();
    }

    @Test
    public void selectByLikeTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserByLike("%j%");
        for(User user:userList){
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void insertTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        Integer res = mapper.insertUser(new User(3, "jxy", "12345"));
        if(res>0){
            System.out.println("插入成功！");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void insertByMapTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId",3);
        map.put("userName","jjj");
        map.put("userPwd","133444");
        Integer res = mapper.insertUserByMap(map);
        if(res>0){
            System.out.println("插入成功！");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        Integer res = mapper.updateUser(new User(3, "jxy", "12345"));
        if(res>0){
            System.out.println("修改成功！");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        mapper.deleteUserById(3);
        sqlSession.commit();
        sqlSession.close();
    }
}
