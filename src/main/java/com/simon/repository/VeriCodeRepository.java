package com.simon.repository;

import com.simon.domain.VeriCode;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by simon on 2016/9/19.
 */
public interface VeriCodeRepository extends MongoRepository<VeriCode, String> {
    VeriCode findByPhone(String phone);
    VeriCode findByPhoneAndCode(String phone, Integer code);
}
