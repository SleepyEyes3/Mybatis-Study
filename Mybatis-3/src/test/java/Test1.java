import com.jxy.dao.TeacherMapper;
import com.jxy.pojo.Teacher;
import com.jxy.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

public class Test1 {
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacherById(1);
        System.out.println(teacher);
    }
}
