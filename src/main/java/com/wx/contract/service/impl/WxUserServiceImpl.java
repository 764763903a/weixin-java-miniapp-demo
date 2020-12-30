package com.wx.contract.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.contract.mapper.WxUserMapper;
import com.wx.contract.domain.WxUser;
import com.wx.contract.service.WxUserService;

@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {

}

