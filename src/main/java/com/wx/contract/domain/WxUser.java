package com.wx.contract.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="com-wx-contract-domain-WxUser")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "wx_user")
public class WxUser implements Serializable {
    /**
     * 用户主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="用户主键")
    private Integer id;

    /**
     * 微信主键
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value="微信主键")
    private String openId;

    /**
     * 姓名
     */
    @TableField(value = "nickname")
    @ApiModelProperty(value="姓名")
    private String nickname;

    /**
     * 个性签名
     */
    @TableField(value = "motto")
    @ApiModelProperty(value="个性签名")
    private String motto;

    /**
     * 认证姓名
     */
    @TableField(value = "student_name")
    @ApiModelProperty(value="认证姓名")
    private String studentName;

    /**
     * 学生院系
     */
    @TableField(value = "faculty")
    @ApiModelProperty(value="学生院系")
    private String faculty;

    /**
     * 学生班级
     */
    @TableField(value = "class_name")
    @ApiModelProperty(value="学生班级")
    private String className;

    /**
     * 学生号
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value="学生号")
    private String studentId;

    /**
     * 姓名
     */
    @TableField(value = "sex")
    @ApiModelProperty(value="姓名")
    private Byte sex;

    /**
     * 个人爱好
     */
    @TableField(value = "love")
    @ApiModelProperty(value="个人爱好")
    private String love;

    /**
     * 生日
     */
    @TableField(value = "born")
    @ApiModelProperty(value="生日")
    private Date born;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 最后上线时间
     */
    @TableField(value = "last_time")
    @ApiModelProperty(value="最后上线时间")
    private Date lastTime;

    /**
     * 微信注册城市
     */
    @TableField(value = "city")
    @ApiModelProperty(value="微信注册城市")
    private String city;

    /**
     * 头像
     */
    @TableField(value = "avatarUrl")
    @ApiModelProperty(value="头像")
    private String avatarurl;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_OPEN_ID = "open_id";

    public static final String COL_NICKNAME = "nickname";

    public static final String COL_MOTTO = "motto";

    public static final String COL_STUDENT_NAME = "student_name";

    public static final String COL_FACULTY = "faculty";

    public static final String COL_CLASS_NAME = "class_name";

    public static final String COL_STUDENT_ID = "student_id";

    public static final String COL_SEX = "sex";

    public static final String COL_LOVE = "love";

    public static final String COL_BORN = "born";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_LAST_TIME = "last_time";

    public static final String COL_CITY = "city";

    public static final String COL_AVATARURL = "avatarUrl";
}