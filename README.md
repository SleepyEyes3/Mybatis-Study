##### **需要注意的点**

##### 1、关于Map传参的使用

* Map传递参数，直接在sql中取出key即可
* 对象传递参数，直接在sql中取对象的属性即可
* 只有一个基本类型参数的情况下，可以直接在sql中取到

##### 2、关于模糊查询

* Java代码执行的时候，传递通配符% %

* 在sql拼接中使用通配符

  尽量使用方式一，因为方式二会导致sql注入

  * 方式一：

    ```java
    List<User> userList = mapper.getUserByLike("%j%");
    ```

  * 方式二：

    ```xml
    <select id="getUserByLike" resultType="com.jxy.pojo.User">
        select * from mybatis.user where name like "%"#{str}"%"
    </select>
    ```

##### 3、xml配置

注意：*xml中的标签的顺序有一定的要求，不能够随意改动标签位置*。

- db.properties

```
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;serverTimezone=UTC&amp;useUnicode=true&amp;characterEncoding=utf8
username=root
password=123456
```

- properties标签内容

```xml
<properties resource="db.properties">
  <property name="username" value="root"/>
  <property name="password" value="123456"/>
</properties>
```

​       注意：当外部配置文件中及property属性中同时配置了一个属性时，外部文件中的配置优先

- typeAliases

```xml
<!--    给实体类设置别名-->
<typeAliases>
    <typeAlias alias="User" type="com.jxy.pojo.User"/>
</typeAliases>
```

```xml
<!--    使用扫描包的方式起别名，默认使用实体类名的小写开头的名称-->
<typeAliases>
  <package name="com.jxy.pojo"/>
</typeAliases>
```

​        *在使用扫描包的方式的情况下，还可以使用注解进行起别名*

```java
@Alias("author")
public class Author {
    ...
}
```

##### 4、一些重要的配置

![image-20200517090519359](C:\Users\10715\AppData\Roaming\Typora\typora-user-images\image-20200517090519359.png)

![image-20200517090611975](C:\Users\10715\AppData\Roaming\Typora\typora-user-images\image-20200517090611975.png)

##### 5、映射器（Mappers）

MapperRegistry注册绑定Mapper文件

```xml
<!-- 使用相对于类路径的资源引用 -->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>

<!--      以下两种方式需要接口文件还有.xml文件同名、同包       -->
<!-- 使用映射器接口实现类的完全限定类名 -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>
<!-- 将包内的映射器接口实现全部注册为映射器 -->
<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
```

##### 6、生命周期与作用域

##### 7、解决属性名与字段名不一致的问题

* 在sql语句中使用as语法

```sql
select id,name,pwd as password from uesr where id=1；
```

* 使用resultMap

```xml
<!-- 
	1. select中的 resultMap要与 resultMap中的id相对应
	2. column表示数据库中的字段名，property表示类中属性名
-->
<resultMap id="userMap" type="user">
    <result column="id" property="id"></result>
    <result column="name" property="name"></result>
    <result column="pwd" property="password"></result>
</resultMap>

<select id="getUserListByResultMap" resultType="user" resultMap="userMap">
    select * from mybatis.user where id = #{id}
</select>
```

##### 8、日志的使用

###### 1、导入依赖

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

###### 2、在xml里面进行配置

```xml
    <settings>
        <!--<setting name="logImpl" value="STDOUT_LOGGING"/>-->
        <setting name="logImpl" value="LOG4J"/>
    </settings>
```

###### 3、设置**log4j.properties**文件的配置

```
#将等级为DEBUG的日志信息输出到console和file两个目的地
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.Target=System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%c]-%m%n

#文件输出的相关配置
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/jxy.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}[%c]%m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

###### 4、使用

```java
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
}
```

##### 9、分页

------

###### 1、使用map传参的方法

* SQL语句的编写

```sql
<select id="getUserLimit" resultType="map" resultMap="userMap">
    select * from mybatis.user limit #{startIndex},#{pageSize}
</select>
```

* 参数的传入

```java
Map<String, Integer> map = new HashMap<String, Integer>();
map.put("startIndex",2);
map.put("pageSize",2);
List<User> userList = mapper.getUserLimit(map);
```

###### 2、RowBounds

###### 3、Mybatis分页插件

##### 10、使用注解开发

------

###### 10.1、这边有一个Mybatis的内部实现逻辑看一下视频17

###### 10.2、注解模式下的CRUD

###### 10.3、关于@Param()注解

* 基本类型的参数或者String类型，需要加上
* 引用类型不需要加
* 如果只有一个基本类型的话，可以忽略，但是建议还是加上
* 我们在SQL中引用的就是@Param()中设定的属性名

###### 10.4Lombok的使用

##### 11、多对一的处理 --> association

###### （1）表的创建

```sql
CREATE TABLE `teacher` (
`id` INT(10) NOT NULL,
`name` VARCHAR(30) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO teacher(`id`, `name`) VALUES (1, '秦老师'); 

CREATE TABLE `student` (
`id` INT(10) NOT NULL,
`name` VARCHAR(30) DEFAULT NULL,
`tid` INT(10) DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `fktid` (`tid`),
CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('1', '小明', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('2', '小红', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('3', '小张', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('4', '小李', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('5', '小王', '1');
```

###### （2）环境搭建

###### （3）查询xml编写

* 嵌套查询模式

```xml
<!--    类似于一个嵌套查询-->
<select id="getStudentList1" resultType="Student" resultMap="StudentTeacher">
    select * from student;
</select>
<resultMap id="StudentTeacher" type="Student">
    <result property="id" column="id"></result>
    <result property="name" column="name"></result>
    <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"></association>
</resultMap>
<select id="getTeacher" resultType="Teacher">
    select * from teacher;
</select>
```

* 关联查询模式（结果驱动的，由SQL语句编写xml语句）

```xml
<!--    结果驱动，由一个关联查询语句进行相应编写-->
<select id="getStudentList2" resultType="Student" resultMap="StudentTeacher2">
    select s.id as sid,s.name sname, t.name tname, t.id tid
    from student as s ,teacher t
    where s.tid = t.id;
</select>
<resultMap id="StudentTeacher2" type="Student">
    <result property="id" column="sid"></result>
    <result property="name" column="sname"></result>
    <association property="teacher" javaType="Teacher">
        <result property="id" column="tid"></result>
        <result property="name" column="tname"></result>
    </association>
</resultMap>
```

##### 12、一对多的处理 --> collection

###### （1）关联查询模式

```xml
<mapper namespace="com.jxy.dao.TeacherMapper">
    <select id="getTeacherById1" resultMap="TeacherStudent">
        select s.id sid,s.name sname,t.id tid,t.name tname
        from teacher t,student s
        where t.id = #{tid} and s.tid = t.id;
    </select>
    <resultMap id="TeacherStudent" type="Teacher">
        <result property="id" column="tid"></result>
        <result property="name" column="tname"></result>
        <collection property="students" ofType="Student">
            <result property="id" column="sid"></result>
            <result property="name" column="sname"></result>
            <result property="tid" column="tid"></result>
        </collection>
    </resultMap>
```

###### （2）子查询模式（嵌套查询）

```xml
<select id="getTeacherById2" resultMap="TeacherStudent2">
    select id,name from teacher where id=#{tid};
</select>
<resultMap id="TeacherStudent2" type="Teacher">
    <result property="id" column="id"></result>
    <result property="name" column="name"></result>
    <collection property="students" javaType="ArrayList" ofType="Student" select="getStudentsByTeacherId" column="id"></collection>
</resultMap>

<select id="getStudentsByTeacherId" resultType="Student">
    select *
    from student
    where tid=#{tid}
</select>
```

