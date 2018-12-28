package cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cf.bean.User;
/*
 * 纯注解，无需sql文件
 */
@Mapper
public interface ManageUserInfo {

	public static final String QUERY_USER_SQL="select id,name,age,sex from userinfo where id=#{id} and name=#{name}";
	
	@Select(QUERY_USER_SQL)
	public User queryUserById(@Param("id") String id,@Param("name") String n);
	
	public static final String QUERY_ALL_USER_SQL="select * from userinfo";
	
	@Select(QUERY_ALL_USER_SQL)
	public List<User> queryAllUser();
	
	@Insert("insert into userinfo(name,age,sex) values(#{name},#{age},#{sex})")
	public int insertUser(User user);
	
	@Update("update userinfo set name=#{name},age=#{age},sex=#{sex} where id=#{id}")
	public int updateUser(User user);
	
	@Delete("delete from userinfo where id=#{id} ")
	public int deleteUser(User user);
}
