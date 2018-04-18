package com.simon.repository;

import com.simon.domain.VeriCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simon on 2016/9/19.
 */
public interface VeriCodeRepository extends JpaRepository<VeriCode, Long> {
    VeriCode findByPhone(String phone);
    VeriCode findByPhoneAndCode(String phone, Integer code);
}
