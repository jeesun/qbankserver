package com.simon;

import com.simon.utils.HttpClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QbankserverApplicationTests {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Test
	public void contextLoads() {
	}

	@Test
	public void pwdEncode(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
		String encodedPwd = encoder.encode("19961120");
		log.error(encodedPwd);
	}

	@Test
	public void HttpClientUtilTest(){
		Map<String, String> map = new LinkedHashMap<>();
		map.put("grant_type", "password");
		map.put("client_id", "clientIdPassword");
		map.put("client_secret", "secret");
		map.put("username", "18860902711");
		map.put("password", "19961120");

		try{
			log.error(HttpClientUtil.post("clientIdPassword", "secret", "http://118.178.141.72:8761/oauth/oauth/token", map, "UTF-8"));
		}catch (IOException e){

		}
	}
}
