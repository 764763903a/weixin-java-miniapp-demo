package com.wx.contract.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.TextDetection;
import com.wx.contract.domain.ListMyFousResult;
import com.wx.contract.domain.Result;
import com.wx.contract.domain.WxFan;
import com.wx.contract.domain.WxUser;
import com.wx.contract.service.WxFanService;
import com.wx.contract.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.wx.contract.config.WxMaConfiguration;
import com.wx.contract.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private WxUserService wxUserService;

    @Resource
    private WxFanService wxFanService;

    /**
     * 登陆接口
     */
    @ApiOperation("用户登录")
    @GetMapping("/login")
    public Result<Object> login(@PathVariable @ApiParam(name="appid",value="小程序appid",required=true) String appid,
                        @ApiParam(name="code",value="微信提供的code",required=true) String code) {
        if (StringUtils.isBlank(code)) {
            return Result.succeed("empty jscode");
        }

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据

            //判断是不是新用户
            WxUser one = wxUserService.getOne(new LambdaQueryWrapper<WxUser>().eq(WxUser::getOpenId, session.getOpenid()));
            if (one!=null){
                return Result.newuser("欢迎新用户注册。");
            }else{
                return Result.succeed(JsonUtils.toJson(session),"登录成功");
            }
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return Result.failed("登录失败"+e.toString());
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @ApiOperation("获取微信用户信息")
    @GetMapping("/info")
    public String info(@PathVariable
                           @ApiParam(name="appid",value="小程序appid",required=true)    String appid,
                       @ApiParam(name="sessionKey",value="会话密钥",required=true) String sessionKey,
                       @ApiParam(name="signature",value="用于校验用户信息",required=true) String signature,
                       @ApiParam(name="rawData",value="不包括敏感信息的原始数据字符串，用于计算签名",required=true) String rawData,
                       @ApiParam(name="encryptedData",value="包括敏感数据在内的完整用户信息的加密数据",required=true) String encryptedData,
                       @ApiParam(name="iv",value="加密算法的初始向量",required=true) String iv) {
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
    @ApiOperation("获取用户绑定手机号信息（个人用户不一定能用）")
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
    public Result<Object> authenticate(@PathVariable @ApiParam(name="name",value="学生姓名",required=true)String name ,
                          @ApiParam(name="faculty",value="学生院系",required=true) String faculty,
                          @ApiParam(name="className",value="学校班级",required=true) String className ,
                          @ApiParam(name="classNum",value="学号",required=true) String classNum,
                          @ApiParam(name="imgUrl",value="图片连接",required=true) String imgUrl,
                          @ApiParam(name="openId",value="用户唯一编号",required=true) String openId){

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
                //校验成功后将数据存入数据库中 做了重复校验的判断
                WxUser one = wxUserService.getOne(new LambdaQueryWrapper<WxUser>().eq(WxUser::getOpenId, openId));
                one.setUser(name,faculty,className,classNum);
                wxUserService.updateById(one);
                return Result.succeed("校验成功");
            }else {
                return Result.failed("校验失败,请重新上传后校验");
            }
        }catch(DuplicateKeyException de){
            return Result.failed("该学生已被绑定，如有问题，请联系客服解绑");
        } catch (TencentCloudSDKException e) {
            logger.error(e.toString());
            return Result.failed("校验失败");
        }
    }

    @GetMapping("/userInfo")
    @ApiOperation("获取学生个人信息")
    public Result<WxUser> getUserInfo(@PathVariable @ApiParam(name="openId",value="用户唯一编号",required=true) String openId){
        return Result.succeed(wxUserService.getOne(new LambdaQueryWrapper<WxUser>().eq(WxUser::getOpenId,openId)));
    }

    @PostMapping("/register")
    @ApiOperation("注册学生账号或编辑学生信息")
    public Result<Boolean> register(@RequestBody @ApiParam(name="wxFan",value="关注对象",required=true) WxUser wxUser){
        return Result.succeed(wxUserService.saveOrUpdate(wxUser));
    }

    @PostMapping("/attention")
    @ApiOperation("关注")
    public Result<Boolean> attention(@RequestBody @ApiParam(name="wxFan",value="关注对象",required=true) WxFan wxFan){
        return Result.succeed(wxFanService.save(wxFan));
    }

    @PostMapping("/cancleAttention")
    @ApiOperation("取消关注")
    public Result<Boolean> cancleAttention(@RequestBody @ApiParam(name="wxUser",value="学生对象",required=true) WxFan wxFan){
        return Result.succeed(wxFanService.remove(new LambdaQueryWrapper<WxFan>()
            .eq(WxFan::getOpenId,wxFan.getOpenId()).eq(WxFan::getBOpenId,wxFan.getBOpenId())));
    }

    @GetMapping("/listMyFocus")
    @ApiOperation("我关注的人")
    public Result<List<ListMyFousResult>> myFocus(String openId){
        List<ListMyFousResult> listMyFousResults = wxFanService.ListMyFous(openId);
        return Result.succeed(listMyFousResults);
    }





}
