package com.simon.controller;

import com.simon.domain.ResultMsg;
import com.simon.domain.UserInfo;
import com.simon.domain.VeriCode;
import com.simon.domain.jdbc.Authority;
import com.simon.domain.jdbc.OauthUser;
import com.simon.domain.token.AccessToken;
import com.simon.repository.AuthorityRepository;
import com.simon.repository.OauthUserRepository;
import com.simon.repository.UserInfoRepository;
import com.simon.repository.VeriCodeRepository;
import com.simon.utils.HttpClientUtil;
import com.simon.utils.ServerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by simon on 2016/8/16.
 */
@Api(value="登录注册", description = "登录注册")
@RestController
@RequestMapping("/api/oauthUsers")
public class OauthUserController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    VeriCodeRepository veriCodeRepository;

    @Autowired
    OauthUserRepository oauthUserRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    private static Logger logger = Logger.getLogger(OauthUserController.class);


    @ApiOperation(value = "登录", notes = "this is notes", httpMethod = "GET")
    @RequestMapping(value = "/{phone}/{password}", method = RequestMethod.GET)
    private ResultMsg get(@PathVariable(value = "phone") String phone,
                          @PathVariable(value = "password") String password) {
        //用户密码被加密了
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);

        OauthUser oauthUser = oauthUserRepository.findByPhone(phone);

        if (null != oauthUser && encoder.matches(password, oauthUser.getPassword())) {
            UserInfo userInfo = userInfoRepository.findByPhone(phone);

            Map<String, String> map = new LinkedHashMap<>();
            map.put("grant_type", "password");
            map.put("client_id", "clientIdPassword");
            map.put("client_secret", "secret");
            map.put("username", userInfo.getUsername());
            map.put("password", password);

            //拿到用户信息和access_token
            AccessToken accessToken = null;
            try {
                accessToken = HttpClientUtil.postAndGetToken("clientIdPassword",
                        "secret", ServerContext.OAUTH_URI, map, "UTF-8");
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("userInfo", userInfo);
                dataMap.put("token", accessToken);
                return new ResultMsg(200, "登录成功", dataMap);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResultMsg(404, "用户名或者密码错误", e.getMessage());
            }
        }else{
            return new ResultMsg(404, "用户名或者密码错误", null);
        }
    }

    @Deprecated
    @ApiOperation(value = "注册", notes = "注册成功返回appUser对象，包含自动生成的username", httpMethod = "POST")
    @RequestMapping(value = "/registerWithVeriCode",method = RequestMethod.POST)
    public ResultMsg post(@RequestParam(required = false) Integer code, @RequestParam String phone, @RequestParam String password) {
        //加密密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        password = encoder.encode(password);

        if(null != code){
            VeriCode veriCode = veriCodeRepository.findByPhoneAndCode(phone, code);
            if (null!=veriCode){
                return register(phone, password);
            }else{
                return new ResultMsg(404, "验证码错误或者过期");
            }
        }else{
            return register(phone, password);
        }
    }

    @Transactional//事务加在接口方法上，会造成JpaRepository未注入。加在普通方法上没有问题。
    public ResultMsg register(String phone, String password){
        //判断username是否存在
        try {
            OauthUser oauthUser = new OauthUser();
            oauthUser.setUsername("user" + phone.substring(phone.length()-4, phone.length()));
            oauthUser.setPhone(phone);
            oauthUser.setPassword(password);
            oauthUser.setEnabled(true);
            oauthUser = oauthUserRepository.save(oauthUser);

            Authority authority = new Authority();
            authority.setUsername(oauthUser.getUsername());
            authority.setAuthority("ROLE_USER");
            authority = authorityRepository.save(authority);

            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(oauthUser.getId());
            userInfo.setPhone(phone);
            userInfo.setUsername(oauthUser.getUsername());

            return new ResultMsg(201, "注册成功", userInfoRepository.save(userInfo));
        } catch (DataIntegrityViolationException e) {
            return new ResultMsg(409, "用户名已存在", e.getMessage());
        }
    }

    @ApiOperation(value = "更新密码（使用旧密码）", notes = "目前密码是明文存储，正式发布前需要做加密")
    @RequestMapping(value = "/updatePassword/{oldPassword}/{newPassword}", method = RequestMethod.PATCH)
    private ResultMsg updatePassword(@RequestParam String access_token, @PathVariable String oldPassword, @PathVariable String newPassword){
        String username = getUsernameByAccessToken(access_token);
        OauthUser oauthUser = oauthUserRepository.findByUsername(username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);

        if (null!=oauthUser){
            if(encoder.matches(oldPassword, oauthUser.getPassword())){
                try{
                    this.jdbcTemplate.update("UPDATE users SET password = ? WHERE username = ?", encoder.encode(newPassword), username);
                    return new ResultMsg(200, "更新密码成功");
                }catch (Exception e){
                    return new ResultMsg(404, "更新密码失败", e.getMessage());
                }
            }else{
                return new ResultMsg(404, "旧密码错误");
            }

        }else {
            return new ResultMsg(404, "手机号尚未注册");
        }
    }

    @ApiOperation(value = "更新密码（使用手机验证码）",notes = "此处还需要传一次验证码，防止有人破解app后知道更新密码api，直接更新其他用户密码")
    @RequestMapping(value = "/updatePwdWithoutOldPwd", method = RequestMethod.PATCH)
    private ResultMsg updatePwdWithoutOldPwd(@RequestParam String phone, @RequestParam Integer code, @RequestParam String newPwd){

        //加密密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        newPwd = encoder.encode(newPwd);

        try{
            VeriCode veriCode = veriCodeRepository.findByPhoneAndCode(phone, code);
            if (null!=veriCode){
                this.jdbcTemplate.update("UPDATE users SET password = ? WHERE username = ?", newPwd, phone);
                return new ResultMsg(200, "更新密码成功");
            }else{
                return new ResultMsg(404, "验证码过期，更新密码失败");
            }
        }catch (Exception e){
            return new ResultMsg(404, "更新密码失败", e.getMessage());
        }
    }

    public OauthUser findOauthUserByPhone(String phone) {
        return jdbcTemplate.queryForObject(
                "SELECT username,password,enabled FROM users where phone=?",
                new Object[]{phone}, (ResultSet resultSet, int i)->{
                    OauthUser oauthUser = new OauthUser();
                    oauthUser.setUsername(resultSet.getString("username"));
                    oauthUser.setPassword(resultSet.getString("password"));
                    oauthUser.setEnabled(resultSet.getBoolean("enabled"));
                    return oauthUser;
                });
    }
    private String getUsernameByAccessToken(String access_token){
        return jdbcTemplate.queryForObject("SELECT user_name FROM oauth_access_token" +
                " WHERE encode(token, 'escape') LIKE CONCAT('%', ?)", new Object[]{access_token}, String.class);
        /*return jdbcTemplate.queryForObject("SELECT user_name FROM oauth_access_token" +
                " WHERE right(cast(token as char), 36)=?", new Object[]{access_token}, String.class);*/
    }
}
