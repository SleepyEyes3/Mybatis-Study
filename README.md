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

##### 13、动态SQL

**数据库表的创建**

```sql
CREATE TABLE `blog` (
`id` varchar(50) NOT NULL COMMENT '博客id',
`title` varchar(100) NOT NULL COMMENT '博客标题',
`author` varchar(30) NOT NULL COMMENT '博客作者',
`create_time` datetime NOT NULL COMMENT '创建时间',
`views` int(30) NOT NULL COMMENT '浏览量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

###### （1）if

```xml
<select id="queryBlogIf" parameterType="map" resultType="Blog">
    select * from blog where
    <if test="title != null">
        title = #{title}
    </if>
    <if test="author != null">
        and author = #{author}
    </if>
</select>
```

###### （2）where +if

**这个“where”标签会知道如果它包含的标签中有返回值的话，它就插入一个‘where’。此外，如果标签返回的内容是以AND 或OR 开头的，则它会剔除掉。**

```xml
<select id="queryBlogIf" parameterType="map" resultType="blog">
  select * from blog
   <where>
       <if test="title != null">
          title = #{title}
       </if>
       <if test="author != null">
          and author = #{author}
       </if>
   </where>
</select>
```

###### （3）set + if

set**标签主要用于去除更新语句中条件后面的逗号**

```xml
<!--注意set是用的逗号隔开-->
<update id="updateBlog" parameterType="map">
  update blog
     <set>
         <if test="title != null">
            title = #{title},
         </if>
         <if test="author != null">
            author = #{author}
         </if>
     </set>
  where id = #{id};
</update>
```

###### （4）where + choose + when + otherwise

有时候，我们不想用到所有的查询条件，**只想选择其中的一个**，查询条件有一个满足即可，使用 choose 标签可以解决此类问题，**类似于 Java 的 switch 语句**

> 其中在有otherwise标签的情况下，需要至少一个参数；
>
> 但是在没有otherwise的情况下，可以不提供任何参数；

```xml
<select id="queryBlogChoose" parameterType="map" resultType="blog">
  select * from blog
   <where>
       <choose>
           <when test="title != null">
                title = #{title}
           </when>
           <when test="author != null">
              and author = #{author}
           </when>
           <otherwise>
              and views = #{views}
           </otherwise>
       </choose>
   </where>
</select>
```

###### （5）SQL代码片段

**有时候可能某个 sql 语句我们用的特别多，为了增加代码的重用性，简化代码，我们需要将这些代码抽取出来，然后使用时直接调用。**

1. 提取SQL片段

```xml
<sql id="if-title-author">
   <if test="title != null">
      title = #{title}
   </if>
   <if test="author != null">
      and author = #{author}
   </if>
</sql>
```

2. 引用SQL片段

```xml
<select id="queryBlogIf" parameterType="map" resultType="blog">
  select * from blog
   <where>
       <!-- 引用 sql 片段，如果refid 指定的不在本文件中，那么需要在前面加上 namespace -->
       <include refid="if-title-author"></include>
       <!-- 在这里还可以引用其他的 sql 片段 -->
   </where>
</select>
```

> 注意：
>
> 1. 最好基于单表来定义SQL片段，提高片段的可重用性；
>
> 2. 在 sql 片段中不要包括 where

###### （6）foreach

```xml
<select id="queryBlogForeach" parameterType="map" resultType="blog">
  select * from blog
   <where>
       <!--
       collection:指定输入对象中的集合属性
       item:每次遍历生成的对象
       open:开始遍历时的拼接字符串
       close:结束时拼接的字符串
       separator:遍历对象之间需要拼接的字符串
       select * from blog where 1=1 and (id=1 or id=2 or id=3)
     -->
       <foreach collection="ids"  item="id" open="and (" close=")" separator="or">
          id=#{id}
       </foreach>
   </where>
</select>	
```

```xml
<select id="queryBlogForeach2" parameterType="list" resultType="blog">
    select * from blog
    <!--
        1. 表达式变成了 select * from blog where id (1,2,3)
        2. 参数的传入由map类型变成了list类型
        3. 加入了list为null时的判断
    -->
    <if test="list != null">
        <where>
            id in
            <foreach collection="list"  item="id" open="(" close=")" separator=",">
                #{id}
            </foreach>
        </where>
    </if>
</select>
```

##### 14、缓存

###### 14.1、Mybatis缓存

（1）MyBatis包含一个非常强大的查询缓存特性，它可以非常方便地定制和配置缓存。缓存可以极大的提升查询效率。

（2）MyBatis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**

- 默认情况下，只有一级缓存开启。（SqlSession级别的缓存，也称为本地缓存）
- 二级缓存需要手动开启和配置，他是基于namespace级别的缓存。
- 为了提高扩展性，MyBatis定义了缓存接口Cache。我们可以通过实现Cache接口来自定义二级缓存

###### 14.2、一级缓存

1. 一级缓存也叫本地缓存：

   * 与数据库同一次会话期间查询到的数据会放在本地缓存中。
   * 以后如果需要获取相同的数据，直接从缓存中拿，没必须再去查询数据库；

2. 一级缓存失效的四种情况

   > *a*. 一级缓存是SqlSession级别的缓存，是一直开启的，我们关闭不了它；
   >
   > *b*. 一级缓存失效情况：没有使用到当前的一级缓存，效果就是，还需要再向数据库中发起一次查询请求！

   * sqlSession不同

     ```java
     @Test
     public void testQueryUserById(){
        SqlSession session = MybatisUtils.getSession();
        SqlSession session2 = MybatisUtils.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        UserMapper mapper2 = session2.getMapper(UserMapper.class);
     
        User user = mapper.queryUserById(1);
        System.out.println(user);
        User user2 = mapper2.queryUserById(1);
        System.out.println(user2);
        System.out.println(user==user2);
     
        session.close();
        session2.close();
     }
     // 观察结果：发现发送了两条SQL语句！
     // 结论：每个sqlSession中的缓存相互独立
     ```

   * sqlSession相同，查询条件不同

   * sqlSession相同，两次查询之间执行了**增删改**操作！

   * sqlSession相同，手动清除一级缓存

     ```java
     @Test
     public void testQueryUserById(){
        SqlSession session = MybatisUtils.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
     
        User user = mapper.queryUserById(1);
        System.out.println(user);
     
        session.clearCache();//手动清除缓存
     
        User user2 = mapper.queryUserById(1);
        System.out.println(user2);
     
        System.out.println(user==user2);
     
        session.close();
     }
     ```

###### 14.3、二级缓存

1. 简介

   - 二级缓存也叫**全局缓存**，一级缓存作用域太低了，所以诞生了二级缓存

   - 基于**namespace**级别的缓存，一个名称空间，对应一个二级缓存；

   - 工作机制

   - - 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     - 如果当前会话关闭了，这个会话对应的一级缓存就没了；但是我们想要的是，会话关闭了，一级缓存中的数据被保存到二级缓存中；
     - 新的会话查询信息，就可以从二级缓存中获取内容；
     - 不同的mapper查出的数据会放在自己对应的缓存（map）中；

2. 使用步骤

   * 开启全局缓存 【mybatis-config.xml】

     ```xml
     <setting name="cacheEnabled" value="true"/>
     ```

   * 去每个mapper.xml中配置使用二级缓存，这个配置非常简单；【xxxMapper.xml】

     ```xml
     <cache/>
     
     官方示例=====>查看官方文档
     <cache
      eviction="FIFO"
      flushInterval="60000"
      size="512"
      readOnly="true"/>
     这个更高级的配置创建了一个 FIFO 缓存，每隔 60 秒刷新，最多可以存储结果对象或列表的 512 个引用，而且返回的对象被认为是只读的，因此对它们进行修改可能会在不同线程中的调用者产生冲突。
     ```

   * 代码测试

     ```java
     @Test
     public void testQueryUserById(){
        SqlSession session = MybatisUtils.getSession();
        SqlSession session2 = MybatisUtils.getSession();
     
        UserMapper mapper = session.getMapper(UserMapper.class);
        UserMapper mapper2 = session2.getMapper(UserMapper.class);
     
        User user = mapper.queryUserById(1);
        System.out.println(user);
        session.close();
     
        User user2 = mapper2.queryUserById(1);
        System.out.println(user2);
        System.out.println(user==user2);
     
        session2.close();
     }
     ```

3. 结论

   - 只要开启了二级缓存，我们在同一个Mapper中的查询，可以在二级缓存中拿到数据
   - 查出的数据都会被默认先放在一级缓存中
   - 只有会话提交或者关闭以后，一级缓存中的数据才会转到二级缓存中

4. 缓存原理图

![image-20200518201042376](C:\Users\10715\AppData\Roaming\Typora\typora-user-images\image-20200518201042376.png)

5. EhCache

