package com.wx.contract.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.TextDetection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.wx.contract.config.WxMaConfiguration;
import com.wx.contract.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Api(value="用户controller",tags={"用户操作接口"})
@RestController
@RequestMapping("/wx/user/{appid}")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${orc.sectetId}")
    private String sectetId ;

    @Value("${orc.secretKey}")
    private String sectetKey;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public String login(@PathVariable String appid, String code) {
        if (StringUtils.isBlank(code)) {
            return "empty jscode";
        }

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据

            //判断是不是新用户


            //新用户进行注册


            //老用户进行登录


            return JsonUtils.toJson(session);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return e.toString();
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public String info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public String phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(phoneNoInfo);
    }

    /**
     * <pre>
     * 学生认证
     * </pre>
     */

    @PostMapping("/auth")
    @ApiOperation("对学生信息进行认证")
    public R authenticate(@PathVariable @ApiParam(name="name",value="学生姓名",required=true)String name ,
                          @ApiParam(name="faculty",value="学生院系",required=true) String faculty,
                          @ApiParam(name="className",value="学校班级",required=true) String className ,
                          @ApiParam(name="classNum",value="学号",required=true) String classNum,
                          @ApiParam(name="imgUrl",value="图片连接",required=true) String imgUrl){

        try{
            Credential cred = new Credential(sectetId, sectetKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            OcrClient client = new OcrClient(cred, "ap-guangzhou", clientProfile);

            GeneralAccurateOCRRequest req = new GeneralAccurateOCRRequest();
            req.setImageUrl(imgUrl);

            GeneralAccurateOCRResponse resp = client.GeneralAccurateOCR(req);
            TextDetection[] textDetections = resp.getTextDetections();
            StringBuilder str = new StringBuilder();

            for (TextDetection textDetection : textDetections) {
                //判断文字提取可信度当大于阈值进行校验
                if (textDetection.getConfidence()>85){
                    String detectedText = textDetection.getDetectedText();
                    //进行去空格防止校验错误  增加校验精度
                    while (detectedText.contains(" ")){
                        detectedText.replace(" ","");
                    }
                    str.append(detectedText);
                }
            }
            logger.info("开始校验："+str);
            //逻辑校验
            String s = str.toString();
            if (s.contains(name)&&s.contains(faculty)&&s.contains(className)&&s.contains(classNum)){
                return R.ok("校验成功");
            }else {
                return R.failed("校验失败,请重新上传");
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

        return null;
    }

}
