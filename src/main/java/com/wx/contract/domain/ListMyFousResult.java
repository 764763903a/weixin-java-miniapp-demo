package com.wx.contract.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@ApiModel(value = "com-wx-contract-domain-ListMyFousResult")
@Data
public class ListMyFousResult implements Serializable {
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String b_open_id;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String avatarUrl;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String nickname;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private Boolean sex;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String motto;

    private static final long serialVersionUID = 1L;
}

