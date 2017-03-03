package com.simon.controller;

import com.simon.domain.AppUser;
import com.simon.repository.AppUserRepository;
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
@RequestMapping("/api/appUserInfo")
public class AppUserController {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ResourceLoader resourceLoader;

    private static final String ROOT = "appUsers";

    @Autowired
    public AppUserController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /*@ApiOperation(value = "\"我的\"模块访问的接口")
    @RequestMapping(value = "/personInfo", method = RequestMethod.GET)
    private Map<String, Object> getPersonInfo(@RequestParam String access_token){
        Map<String, Object> responseMap = new LinkedHashMap<>();

        try{
            String phone = getPhoneByAccessToken(access_token);
            AppUser appUser = appUserRepository.findByPhone(phone);
            PersonInfo personInfo = new PersonInfo();
            personInfo.setAppUser(appUser);

            personInfo.setSignUpCount(joinEventRepository.countByPhone(phone));
            List<JoinEvent> joinEventList = joinEventRepository.getByPhoneAndStatus(phone, ServerContext.SIGN_OUT_STATUS);
            personInfo.setJoinCount(joinEventList.size());

            int volHour = 0;
            for(JoinEvent joinEvent : joinEventList){
                OrgEvent orgEvent = orgEventRepository.findById(joinEvent.getEventId());
                Long beginTime = orgEvent.getBeginTime();
                Long endTime = orgEvent.getEndTime();
                volHour+=(endTime-beginTime)/(1000*60&60);//java时间戳13位，计算到毫秒，这里是计算两个时间戳之间的小时差
            }
            personInfo.setVolHour(volHour);

            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "获取用户信息成功");
            responseMap.put(ServerContext.DATA, personInfo);

        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "获取用户信息失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }

        return responseMap;
    }*/

    @ApiOperation(value="根据access token获取用户信息")
    @RequestMapping(value = "/accessToken",method = RequestMethod.GET)
    private Map<String, Object> getUserInfoByAccessToken(@RequestParam String access_token){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "获取用户信息成功");
            responseMap.put(ServerContext.DATA, appUserRepository.findByPhone(phone));
        }catch (DataRetrievalFailureException e) {
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "获取用户信息失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
    }

    @ApiOperation(value="根据userId获取用户信息")
    @RequestMapping(value = "/id/{userId}",method = RequestMethod.GET)
    private Map<String, Object> getUserInfoById(@PathVariable String userId){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "获取用户信息成功");
            responseMap.put(ServerContext.DATA, appUserRepository.findById(userId));
        }catch (DataRetrievalFailureException e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "获取用户信息失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
    }

    @ApiOperation(value="根据username获取用户信息")
    @RequestMapping(value = "/username/{username}",method = RequestMethod.GET)
    private Map<String, Object> getUserInfoByUsername(@PathVariable String username){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "获取用户信息成功");
            responseMap.put(ServerContext.DATA, appUserRepository.findByUsername(username));
        }catch (DataRetrievalFailureException e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "获取用户信息失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
    }

    @ApiOperation(value = "更新头像")
    @RequestMapping(value = "/updateHeadPhoto", method = RequestMethod.PATCH)
    private Map<String, Object> updateHeadPhoto(@RequestParam String access_token, @RequestParam String photoBase64){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        String headPhotoUrl = ROOT+"/"+appUser.getPhone() + "/" + System.currentTimeMillis() + ".png";
        String headPhotoDir = ROOT + "/" + appUser.getPhone();
        try{
            if (!Files.exists(Paths.get(headPhotoDir))){
                Files.createDirectories(Paths.get(headPhotoDir));
                if (!Files.exists(Paths.get(headPhotoUrl))){
                    Files.createFile(Paths.get(headPhotoUrl));
                }
            }

            Files.write(Paths.get(headPhotoUrl), ImageUtil.convertToBytes(photoBase64));
            appUser.setHeadPhoto(headPhotoUrl);
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新头像成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (IOException e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "创建文件夹或者文件失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 500);
            responseMap.put(ServerContext.MSG, "未知错误");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
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
    private Map<String, Object> updateUsername(@RequestParam String access_token, @RequestParam String username){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        appUser.setUsername(username);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新用户名成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "更新用户名失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }

        return responseMap;
    }

    @ApiOperation(value = "更新邮箱")
    @RequestMapping(value = "/updateEmail", method = RequestMethod.PATCH)
    private Map<String, Object> updateEmail(@RequestParam String access_token, @RequestParam String email){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        appUser.setEmail(email);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新邮箱成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "更新邮箱失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }

        return responseMap;
    }

    @ApiOperation(value = "更新性别")
    @RequestMapping(value = "/updateSex", method = RequestMethod.PATCH)
    private Map<String, Object> updateSex(@RequestParam String access_token, @RequestParam Boolean sex){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        appUser.setSex(sex);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新性别成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "更新性别失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }

        return responseMap;
    }

    @ApiOperation(value = "更新生日")
    @RequestMapping(value = "/updateBirth", method = RequestMethod.PATCH)
    private Map<String, Object> updateBirth(@RequestParam String access_token, @RequestParam String birth){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        appUser.setBirth(birth);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新生日成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "更新生日失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
    }

    @ApiOperation(value="更新年龄")
    @RequestMapping(value = "/updateAge", method = RequestMethod.PATCH)
    private Map<String, Object> updateAge(@RequestParam String access_token, @RequestParam Integer age){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        appUser.setAge(age);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新年龄成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "更新年龄失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
    }

    @ApiOperation(value="更新个人简介")
    @RequestMapping(value = "/updatePersonBrief", method = RequestMethod.PATCH)
    private Map<String, Object> updatePersonBrief(@RequestParam String access_token, @RequestParam String personBrief){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        appUser.setPersonBrief(personBrief);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新个人简介成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "更新个人简介失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
    }

    @ApiOperation(value="更新地址")
    @RequestMapping(value = "/updateAddress", method = RequestMethod.PATCH)
    private Map<String, Object> updateAddress(@RequestParam String access_token, @RequestParam String address){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        String phone = getPhoneByAccessToken(access_token);
        AppUser appUser = appUserRepository.findByPhone(phone);
        appUser.setAddress(address);
        try{
            responseMap.put(ServerContext.STATUS_CODE, 200);
            responseMap.put(ServerContext.MSG, "更新地址成功");
            responseMap.put(ServerContext.DATA, appUserRepository.save(appUser));
        }catch (Exception e){
            responseMap.put(ServerContext.STATUS_CODE, 404);
            responseMap.put(ServerContext.MSG, "更新地址失败");
            responseMap.put(ServerContext.DEV_MSG, e.getMessage());
        }
        return responseMap;
    }

    private String getPhoneByAccessToken(String access_token){
        return jdbcTemplate.queryForObject("SELECT user_name FROM oauth_access_token" +
                " WHERE encode(token, 'escape') LIKE CONCAT('%', ?)", new Object[]{access_token}, String.class);
    }
}
