package com.wx.contract.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="com-wx-contract-domain-WxFan")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "wx_fan")
public class WxFan implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Integer id;

    /**
     * 微信主键
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value="微信主键")
    private String openId;

    /**
     * 被关注微信主键
     */
    @TableField(value = "b_open_id")
    @ApiModelProperty(value="被关注微信主键")
    private String bOpenId;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_OPEN_ID = "open_id";

    public static final String COL_B_OPEN_ID = "b_open_id";
}