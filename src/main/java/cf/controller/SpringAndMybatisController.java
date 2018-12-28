package cf.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;

import cf.bean.PageBean;
import cf.bean.User;
import cf.dao.ManageUserInfo;
import cf.dao.RedisDao;
import cf.dao.UserInfoManage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/infomanage")
public class SpringAndMybatisController {

	@RequestMapping("/getuserdetail")
	public User getUserDetail(@RequestBody User user) {
		String id=user.getId();
		String name=user.getName();
		User queryUserById = manageUserInfo.queryUserById(id,name);
		return queryUserById;
	}
	
	@RequestMapping("/getUserInfo")
	public User getUserInfo(@RequestBody User user) {
		
		User selectUserById = userInfoManage.selectUserById(user.getId());
		return selectUserById;
	}
	
	@ApiOperation(value="获取用户信息",notes="获取所有用户信息")
	@RequestMapping(value="/getalluser",method=RequestMethod.POST)
	@Cacheable("alluser")
	public List<User> getAllUser(@RequestBody PageBean page){
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		List<User> queryAllUser = manageUserInfo.queryAllUser();
//		PageInfo<User> pageInfo=new PageInfo<User>(queryAllUser);
		return queryAllUser;
	}
	
	@ApiOperation(value="插入用户信息")
	@ApiImplicitParam(required = true, dataType = "cf.bean.User", paramType = "body")
	@RequestMapping(value="/insertUser",method=RequestMethod.POST)
	public boolean insertUser(@RequestBody User user) {
		redis.setKey("cyy", user.getName());
		String value = redis.getValue("unknow");
		System.out.println(value);
		redis.setHashKey("famaily", "son", user.getName());
		int insertUser = manageUserInfo.insertUser(user);
		return insertUser>0?true:false;
	}
	
	@RequestMapping("/updateUser")
	public boolean updateUser(@RequestBody User user) {
		int updateUser = manageUserInfo.updateUser(user);
		return updateUser>0?true:false;
	}
	
	@RequestMapping("/deleteUser")
	public boolean deleteUser(@RequestBody User user) {
		int deleteUser = manageUserInfo.deleteUser(user);
		return deleteUser>0?true:false;
	}
	
	
//	@Scheduled(fixedRate=2000)
	public void timing() {
		System.out.println("当前时间："+LocalDate.now());
	}
	
	@Autowired
	ManageUserInfo manageUserInfo;
	
	@Autowired
	private UserInfoManage userInfoManage;
	
	@Autowired
	private RedisDao redis;
}

