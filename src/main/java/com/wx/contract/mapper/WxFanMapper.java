package com.wx.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.contract.domain.ListMyFansResult;
import com.wx.contract.domain.ListMyFousResult;
import com.wx.contract.domain.WxFan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WxFanMapper extends BaseMapper<WxFan> {
    /**
     * 查看我的关注
     *
     * @return
     */
    List<ListMyFousResult> ListMyFous(String openid);

    /**
     * 查看我的粉丝
     * @return
     */
    List<ListMyFansResult> ListMyFans();
}
