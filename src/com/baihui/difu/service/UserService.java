package com.baihui.difu.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baihui.difu.baihui.DataToCrm;
import com.baihui.difu.dao.UserDao;
import com.baihui.difu.entity.User;
import com.baihui.difu.util.PropertyManager;

/**
 * 用户服务类
 * @author huizijing
 * @date 2014/11/17
 */
public class UserService {
	 private Logger log = LoggerFactory.getLogger(this.getClass());
	 UserDao userDao=new UserDao();

	    /**
	      * 从官网获取项目记录同步到本地DB
	     * @throws InterruptedException 
	     * @throws JSONException 
	     * @throws IOException 
	     * @throws ClassNotFoundException 
	     */
		public void batchInsertToDB() throws IOException, JSONException, InterruptedException, ClassNotFoundException{
			List<User> users = new ArrayList<User>();
			
			List<Map<String, String>> lst=DataToCrm.getUsers( PropertyManager.getProperty("CRM_TOKEN"), "ActiveUsers");
			for (int i = 0; i < lst.size(); i++) {
				User user=insertParams(lst.get(i));
				if(userDao.getUserBybaihuiId(user.getBaihuiId()).getJob()==null){
					users.add(user);
				}
			}
			try {
				userDao.save(users);
			} catch (Exception e) {
				log.error("用户同步出错：", e);
			}
		
		}
		
		public User insertParams(Map<String,String> map){
			User user = new User();
			user.setBaihuiId(map.get("id"));//baihuiid
			user.setUserName(map.get("content"));//用户名
			user.setEmail(map.get("email"));//邮箱
			if(map.get("content").contains("-")){
				user.setJob(map.get("content").split("-")[1]);
			}else{
				user.setJob(map.get("role"));
			}
			
			return user;
		}
}
