package com.simon.controller;

import com.simon.domain.QrCode;
import com.simon.domain.ResultMsg;
import com.simon.repository.QrCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrCodes")
public class QrCodeController {
    @Autowired
    private QrCodeRepository qrCodeRepository;
    /**
     * 手机扫描网页端二维码码登录需要访问的接口
     * @param username
     * @param access_token
     * @param sid
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/loginByQrCode")
    public ResultMsg postLoginByQrCode(
            @RequestParam String username,
            @RequestParam String access_token,
            @RequestParam String sid){
        QrCode qrCode = qrCodeRepository.findBySid(sid);
        if (null != qrCode){
            qrCode.setUsername(username);
            qrCode.setToken(access_token);
            qrCode.setOk(true);
            qrCodeRepository.save(qrCode);
            return new ResultMsg(200, "扫码成功", null);
        }else{
            return new ResultMsg(404, "扫码失败，未找到该二维码对应的数据", null);
        }
    }
}
