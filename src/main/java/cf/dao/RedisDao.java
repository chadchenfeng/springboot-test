package cf.dao;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisDao {

	@Autowired
	private StringRedisTemplate template;
	
	public  void setKey(String key,String value){
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key,value,1, TimeUnit.MINUTES);//1分钟过期
    }

    public String getValue(String key){
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }
    
    public void setHashKey(String key,String mapkey,String mapvalue) {
    	 HashOperations<String, Object, Object> opsForHash = template.opsForHash();
    	 opsForHash.put(key, mapkey, mapvalue);
    }
}
