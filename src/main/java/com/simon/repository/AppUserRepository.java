package com.simon.repository;

import com.simon.domain.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by simon on 2016/8/13.
 */
public interface AppUserRepository extends MongoRepository<AppUser, String> {
    AppUser findByUsername(String username);
    AppUser findByPhone(String phone);
    AppUser findById(String id);
}
