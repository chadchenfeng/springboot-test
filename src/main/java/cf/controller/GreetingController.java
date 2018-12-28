package cf.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cf.bean.Greeting;
import cf.bean.User;
@RestController
@EnableAutoConfiguration
public class GreetingController {
	
	private AtomicLong counter=new AtomicLong();
	@RequestMapping(value="/greet",method=RequestMethod.POST)
	public Greeting greeting(@RequestBody User user) {
		System.out.println("===="+user.getName());
		return new Greeting(counter.incrementAndGet(),user.getName());
	}
	
	@RequestMapping("/showDetail")
	public User showDetail() {
		User user=new User();
		user.setAge(String.valueOf(age));
		user.setName(name);
		user.setSex(sex);
		return user;
	}
	
	@Value("${my.name}")
	private String name;
	@Value("${my.age}")
	private int age;
	@Value("${my.sex}")
	private String sex;
	
}
