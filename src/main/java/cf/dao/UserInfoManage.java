package cf.dao;

import org.apache.ibatis.annotations.Mapper;

import cf.bean.User;
/*
 * 需要使用sql文件
 */
@Mapper
public interface UserInfoManage {
	
	public User selectUserById(String id);
}
