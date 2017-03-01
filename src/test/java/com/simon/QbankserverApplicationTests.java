package com.simon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

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
}
