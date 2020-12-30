package com.wx.contract.service;

import com.wx.contract.domain.ListMyFousResult;
import com.wx.contract.domain.WxFan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface WxFanService extends IService<WxFan>{

    /**
     * 查看我的关注
     * @return
     */
    List<ListMyFousResult> ListMyFous(String openid);


}
