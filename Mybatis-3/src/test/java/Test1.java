import com.jxy.dao.StudentMapper;
import com.jxy.dao.TeacherMapper;
import com.jxy.pojo.Student;
import com.jxy.pojo.Teacher;
import com.jxy.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacherById(1);
        System.out.println(teacher);
    }

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
}
