package com.simon.controller;

import com.simon.domain.ResultMsg;
import com.simon.domain.VeriCode;
import com.simon.repository.VeriCodeRepository;
import com.simon.utils.ServerContext;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by simon on 2016/9/19.
 */
@Api(value = "验证码", description = "验证码")
@RestController
@RequestMapping("/api/veriCodes")
public class VeriCodeController {
    @Autowired
    private VeriCodeRepository veriCodeRepository;

    @ApiOperation(value = "获取验证码", notes = "验证码有效时间是30分钟，验证码在失效前5分钟就会重新生成一个返回，给用户通过验证码修改密码足够的时间")
    /*@ApiImplicitParam(name = "phone", value = "用户手机号", required = true, dataType = "string")*/
    @RequestMapping(value = "/getRegisterCode", method = RequestMethod.GET)
    private ResultMsg getVeriCode(@RequestParam String phone){
        try{
            VeriCode veriCode = veriCodeRepository.findByPhone(phone);
            if (null==veriCode){
                veriCode = new VeriCode();
                veriCode.setPhone(phone);
                veriCode.setCode((int)Math.random()*899999+100000);
                veriCode.setCreateTime(System.currentTimeMillis());
                veriCode.setExpires(30*60);
                veriCodeRepository.save(veriCode);
            }else{
                if (System.currentTimeMillis()>(veriCode.getCreateTime()+veriCode.getExpires()-60*5)){
                    veriCode.setCode((int)(Math.random()*899999)+100000);
                    veriCode.setCreateTime(System.currentTimeMillis());
                    veriCode.setExpires(30*60);
                    veriCodeRepository.save(veriCode);
                }
            }
            TaobaoClient client = new DefaultTaobaoClient(
                    ServerContext.DAYU_URL_REAL, ServerContext.DAYU_APP_KEY, ServerContext.DAYU_APP_SECRET);
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend("");
            req.setSmsType("normal");
            req.setSmsFreeSignName(ServerContext.DAYU_SMS_FREE_SIGN_NAME);
            req.setSmsParamString("{veriCode:'"+veriCode.getCode()+"'}");
            req.setRecNum(phone);
            req.setSmsTemplateCode(ServerContext.DAYU_SMS_TEMPLATE_CODE);
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            System.out.println(rsp.getBody());
            if (rsp.getResult().getSuccess()){
                return new ResultMsg(200, "验证码已发送");
            }else{
                return new ResultMsg(200, "验证码发送失败，请稍后重试");
            }

        }catch (Exception e){
            return new ResultMsg(500, e.getMessage());
        }
    }

    @ApiOperation(value = "校验验证码")
    @RequestMapping(value = "/checkVeriCode", method = RequestMethod.GET)
    private ResultMsg checkVeriCode(@RequestParam String phone, @RequestParam Integer code){
        try{
            VeriCode veriCode = veriCodeRepository.findByPhoneAndCode(phone, code);
            if (null!=veriCode){
                return new ResultMsg(200, "验证码正确");
            }else{
                return new ResultMsg(404, "验证码错误");
            }
        }catch (Exception e){
            return new ResultMsg(404, "验证码错误", e.getMessage());
        }
    }
}
