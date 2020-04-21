package org.zyx.entity;

import lombok.Data;

/**
 * Created by SunShine on 2020/4/21.
 */
@Data
public class WxParam {

    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
    private String token;

}
