package com.lightcode.starter.oauth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lijunping on 2021/10/22
 */
@Data
public class BaseLoginRequest implements Serializable {

    @NotBlank(message = "用户类型不能为空")
    private String userType;
}
