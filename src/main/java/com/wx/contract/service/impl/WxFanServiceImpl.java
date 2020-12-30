package com.wx.contract.service.impl;

import com.wx.contract.domain.ListMyFansResult;
import com.wx.contract.domain.ListMyFousResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.contract.mapper.WxFanMapper;
import com.wx.contract.domain.WxFan;
import com.wx.contract.service.WxFanService;
@Service
public class WxFanServiceImpl extends ServiceImpl<WxFanMapper, WxFan> implements WxFanService{

    @Resource
    private WxFanMapper wxFanMapper;


    /**
     * 查看我的关注
     * @return
     */
    @Override
    public List<ListMyFousResult> ListMyFous(String openid) {
        return wxFanMapper.ListMyFous(openid);
    }

    public List<ListMyFansResult> ListMyFans() {
        return wxFanMapper.ListMyFans();
    }
}
