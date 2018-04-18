package com.simon.repository;

import com.simon.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 2016/8/13.
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);
    UserInfo findByPhone(String phone);
    UserInfo findById(String id);
}
