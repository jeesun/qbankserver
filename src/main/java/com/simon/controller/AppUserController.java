package com.simon.controller;

import com.simon.repository.AppUserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by simon on 2017/2/26.
 */
@Api(value = "用户信息", description = "用户信息")
@RestController
@RequestMapping("/api/appUsers")
public class AppUserController {
    @Autowired
    private AppUserRepository appUserRepository;

}
