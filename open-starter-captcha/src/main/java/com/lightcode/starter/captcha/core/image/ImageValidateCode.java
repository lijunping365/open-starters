package com.lightcode.starter.captcha.core.image;

import com.lightcode.starter.captcha.core.sms.ValidateCode;
import lombok.Data;
import java.awt.image.BufferedImage;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * ImageCode对象没有实现Serializable 接口，
 * 且属性 --- BufferedImage对象，也没有实现Serializable 接口，因此将该对象存到redis里时会报出序列化错误。
 * <p>
 * 经过分析可知，我们只需要把验证码和过期时间放到redis里就行了，因为图形验证码验证的是验证码，不是图形
 * 所以没必要把BufferedImage对象存到session和redis里。
 * 父类实现了Serializable 就当子类也实现了，所以可以只在其父类实现就好了
 */
@Data
public class ImageValidateCode extends ValidateCode{
  private static final long serialVersionUID = 8060570961769862234L;
  /**
   * 图片
   */
  private BufferedImage image;


  /**
   * 构造-----expireIn 超时时间（单位秒）
   *
   * @param image
   * @param code
   * @param expireIn
   */
  public ImageValidateCode(BufferedImage image, String code, int expireIn) {
    super(code, expireIn);
    this.image = image;
  }

}
