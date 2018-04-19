package com.simon.controller;

import com.simon.domain.ResultMsg;
import com.simon.domain.UserInfo;
import com.simon.repository.UserInfoRepository;
import com.simon.utils.ImageUtil;
import com.simon.utils.ServerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by simon on 2016/8/21.
 */
@Api(value="用户信息", description = "用户信息")
@RestController
@RequestMapping("/api/userInfos")
public class UserInfoController {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ResourceLoader resourceLoader;

    private static final String ROOT = "appUsers";

    @Autowired
    public UserInfoController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @ApiOperation(value="根据access token获取用户信息")
    @RequestMapping(value = "/accessToken",method = RequestMethod.GET)
    private ResultMsg getUserInfoByAccessToken(@RequestParam String access_token){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        try{
            return new ResultMsg(200, "获取用户信息成功", userInfoRepository.findByPhone(phone));
        }catch (DataRetrievalFailureException e) {
            return new ResultMsg(404, "获取用户信息失败", e.getMessage());
        }
    }

    @ApiOperation(value="根据userId获取用户信息")
    @RequestMapping(value = "/id/{userId}",method = RequestMethod.GET)
    private ResultMsg getUserInfoById(@PathVariable String userId){
        try{
            return new ResultMsg(200, "获取用户信息成功", userInfoRepository.findById(userId));
        }catch (DataRetrievalFailureException e) {
            return new ResultMsg(404, "获取用户信息失败", e.getMessage());
        }
    }

    @ApiOperation(value="根据username获取用户信息")
    @RequestMapping(value = "/username/{username}",method = RequestMethod.GET)
    private ResultMsg getUserInfoByUsername(@PathVariable String username){
        try{
            return new ResultMsg(200, "获取用户信息成功", userInfoRepository.findByUsername(username));
        }catch (DataRetrievalFailureException e) {
            return new ResultMsg(404, "获取用户信息失败", e.getMessage());
        }
    }

    @ApiOperation(value = "更新头像")
    @RequestMapping(value = "/updateHeadPhoto", method = RequestMethod.PATCH)
    private ResultMsg updateHeadPhoto(@RequestParam String access_token, @RequestParam String photoBase64){
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        String headPhotoUrl = ROOT+"/"+userInfo.getPhone() + "/" + System.currentTimeMillis() + ".png";
        String headPhotoDir = ROOT + "/" + userInfo.getPhone();
        try{
            if (!Files.exists(Paths.get(headPhotoDir))){
                Files.createDirectories(Paths.get(headPhotoDir));
                if (!Files.exists(Paths.get(headPhotoUrl))){
                    Files.createFile(Paths.get(headPhotoUrl));
                }
            }

            Files.write(Paths.get(headPhotoUrl), ImageUtil.convertToBytes(photoBase64));
            userInfo.setHeadPhoto(headPhotoUrl);
            return new ResultMsg(200, "更新头像成功", userInfoRepository.save(userInfo));
        }catch (IOException e){
            return new ResultMsg(404, "创建文件夹或者文件失败", e.getMessage());
        }catch (Exception e){
            return new ResultMsg(500, "未知错误", e.getMessage());
        }
    }

    @ApiOperation(value = "获取头像")
    @RequestMapping(value = "/{baseFolder}/{phoneFolder}/{fileName:.+}", method = RequestMethod.GET)
    private ResponseEntity<?> getFile(@PathVariable("baseFolder")String root, @PathVariable("phoneFolder")String phoneFolder, @PathVariable("fileName")String fileName){
        try{
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(root+"/"+phoneFolder, fileName).toString()));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "更新用户名")
    @RequestMapping(value = "/updateUsername", method = RequestMethod.PATCH)
    private ResultMsg updateUsername(@RequestParam String access_token, @RequestParam String username){
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        userInfo.setUsername(username);
        try{
            return new ResultMsg(200, "更新用户名成功", userInfoRepository.save(userInfo));
        }catch (Exception e){
            return new ResultMsg(404, "更新用户名失败", e.getMessage());
        }
    }

    @ApiOperation(value = "更新邮箱")
    @RequestMapping(value = "/updateEmail", method = RequestMethod.PATCH)
    private ResultMsg updateEmail(@RequestParam String access_token, @RequestParam String email){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        userInfo.setEmail(email);
        try{
            return new ResultMsg(200, "更新邮箱成功", userInfoRepository.save(userInfo));
        }catch (Exception e){
            return new ResultMsg(404, "更新邮箱失败", e.getMessage());
        }
    }

    @ApiOperation(value = "更新性别")
    @RequestMapping(value = "/updateSex", method = RequestMethod.PATCH)
    private ResultMsg updateSex(@RequestParam String access_token, @RequestParam Boolean sex){
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        userInfo.setSex(sex);
        try{
            return new ResultMsg(200, "更新性别成功", userInfoRepository.save(userInfo));
        }catch (Exception e){
            return new ResultMsg(404,"更新性别失败", e.getMessage());
        }
    }

    @ApiOperation(value = "更新生日")
    @RequestMapping(value = "/updateBirth", method = RequestMethod.PATCH)
    private ResultMsg updateBirth(@RequestParam String access_token, @RequestParam String birth){
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        userInfo.setBirth(birth);
        try{
            return new ResultMsg(200, "更新生日成功", userInfoRepository.save(userInfo));
        }catch (Exception e){
            return new ResultMsg(404, "更新生日失败", e.getMessage());
        }
    }

    @ApiOperation(value="更新年龄")
    @RequestMapping(value = "/updateAge", method = RequestMethod.PATCH)
    private ResultMsg updateAge(@RequestParam String access_token, @RequestParam Integer age){
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        userInfo.setAge(age);
        try{
            return new ResultMsg(200, "更新年龄成功", userInfoRepository.save(userInfo));
        }catch (Exception e){
            return new ResultMsg(404, "更新年龄失败", e.getMessage());
        }
    }

    @ApiOperation(value="更新个人简介")
    @RequestMapping(value = "/updatePersonBrief", method = RequestMethod.PATCH)
    private ResultMsg updatePersonBrief(@RequestParam String access_token, @RequestParam String personBrief){
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        userInfo.setPersonBrief(personBrief);
        try{
            return new ResultMsg(200, "更新个人简介成功", userInfoRepository.save(userInfo));
        }catch (Exception e){
            return new ResultMsg(404, "更新个人简介失败", e.getMessage());
        }
    }

    @ApiOperation(value="更新地址")
    @RequestMapping(value = "/updateAddress", method = RequestMethod.PATCH)
    private ResultMsg updateAddress(@RequestParam String access_token, @RequestParam String address){
        String phone = getPhoneByAccessToken(access_token);
        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        userInfo.setAddress(address);
        try{
            return new ResultMsg(200, "更新地址成功", userInfoRepository.save(userInfo));
        }catch (Exception e){
            return new ResultMsg(404, "更新地址失败", e.getMessage());
        }
    }

    private String getPhoneByAccessToken(String access_token){
        return jdbcTemplate.queryForObject("SELECT user_name FROM oauth_access_token" +
                " WHERE encode(token, 'escape') LIKE CONCAT('%', ?)", new Object[]{access_token}, String.class);
        /*return jdbcTemplate.queryForObject("SELECT user_name FROM oauth_access_token" +
                " WHERE right(cast(token as char), 36)=?", new Object[]{access_token}, String.class);*/
    }
}
