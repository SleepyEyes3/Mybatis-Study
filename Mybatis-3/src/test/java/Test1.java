import com.jxy.dao.BlogMapper;
import com.jxy.dao.StudentMapper;
import com.jxy.dao.TeacherMapper;
import com.jxy.pojo.Blog;
import com.jxy.pojo.Student;
import com.jxy.pojo.Teacher;
import com.jxy.utils.IDUtil;
import com.jxy.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacherById(1);
        System.out.println(teacher);
    }

    // 多对一、一对多查询部分
    @Test
    public void StudentListTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentList = studentMapper.getStudentList();
        for (Student student : studentList) {
            System.out.println(student);
        }
        sqlSession.close();
    }

    @Test
    public void StudentListTest1(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentList = studentMapper.getStudentList1();
        for (Student student : studentList) {
            System.out.println(student);
        }
        sqlSession.close();
    }

    @Test
    public void StudentListTest2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentList = studentMapper.getStudentList2();
        for (Student student : studentList) {
            System.out.println(student);
        }
        sqlSession.close();
    }

    @Test
    public void TeacherTest1(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper studentMapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = studentMapper.getTeacherById1(1);
        System.out.println(teacher);
        sqlSession.close();
    }

    @Test
    public void TeacherTest2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper studentMapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = studentMapper.getTeacherById2(1);
        System.out.println(teacher);
        sqlSession.close();
    }

    // 动态SQL部分
    @Test
    public void addInitBlog(){
        SqlSession session = MybatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        Blog blog = new Blog();
        blog.setId(IDUtil.getId());
        blog.setTitle("Mybatis如此简单");
        blog.setAuthor("狂神说");
        blog.setCreateTime(new Date());
        blog.setViews(9999);

        mapper.addBlog(blog);

        blog.setId(IDUtil.getId());
        blog.setTitle("Java如此简单");
        mapper.addBlog(blog);
        blog.setId(IDUtil.getId());
        blog.setTitle("Spring如此简单");

        mapper.addBlog(blog);

        blog.setId(IDUtil.getId());
        blog.setTitle("微服务如此简单");
        mapper.addBlog(blog);

        session.close();
    }

    @Test
    public void testQueryBlogIf(){
        SqlSession session = MybatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title","Mybatis如此简单");
        map.put("author","狂神说");
        List<Blog> blogs = mapper.queryBlogIf(map);

        System.out.println(blogs);

        session.close();
    }

    @Test
    public void testUpdateBlog(){
        SqlSession session = MybatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title","动态SQL");
//        map.put("author","秦疆");
        map.put("id","9d6a763f5e1347cebda43e2a32687a77");

        mapper.updateBlog(map);

        session.close();
    }

    @Test
    public void testQueryBlogChoose(){
        SqlSession session = MybatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("title","Java如此简单");
//        map.put("author","狂神说");
//        map.put("views",9999);
        map.put("title",null);
        map.put("author",null);
        map.put("views",null);
        List<Blog> blogs = mapper.queryBlogChoose(map);

        System.out.println(blogs);

        session.close();
    }

    @Test
    public void testQueryBlogForeach1(){
        SqlSession session = MybatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        HashMap map = new HashMap();
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        map.put("ids",ids);

        List<Blog> blogs = mapper.queryBlogForeach1(map);

        System.out.println(blogs);

        session.close();
    }

    @Test
    public void testQueryBlogForeach2(){
        // 在这个foreach的使用过程中，改变了两个东西
        //  1. 将传入的参数的类型从map换成了list
        //  2. 用了 id in list的表达方式
        //  3. 添加了list为null的时候的判断；
        SqlSession session = MybatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        HashMap map = new HashMap();
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        map.put("ids",ids);
        // list为null时的情况
        ids = null;
        List<Blog> blogs = mapper.queryBlogForeach2(ids);

        System.out.println(blogs);

        session.close();
    }
}
