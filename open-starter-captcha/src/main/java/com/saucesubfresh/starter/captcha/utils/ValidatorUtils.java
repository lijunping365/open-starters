/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.captcha.utils;



import javax.validation.*;
import java.util.Set;
import java.util.stream.Collectors;


public interface ValidatorUtils {

  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  /**
   * 对将要进行请求的 Bean 进行校验
   *
   * @param t      带校验的 bean
   * @param groups 校验组
   * @throws ValidationException 校验失败抛出
   */
  static <T> void validate(T t, Class<?>... groups) throws ValidationException {
    Set<ConstraintViolation<T>> validatedResult = validator.validate(t, groups);
    if (!validatedResult.isEmpty()) {
      ErrorMessage errorMessage = new ErrorMessage().setBeanClass(t.getClass());
      Set<ErrorMessageDetail> errorMessageDetailSet = validatedResult.stream().map(ValidatorUtils::buildErrorMessage).collect(Collectors.toSet());
      errorMessage.setDetailList(errorMessageDetailSet);
      throw new ValidationException(errorMessage.toString());
    }
  }

  /**
   * 构造检验错误信息
   *
   * @param violation 校验结果
   * @return {@link ErrorMessageDetail} 错误信息
   */
  static <T> ErrorMessageDetail buildErrorMessage(ConstraintViolation<T> violation) {
    Path propertyPath = violation.getPropertyPath();
    String message = violation.getMessage();
    Object invalidValue = violation.getInvalidValue();
    return new ErrorMessageDetail().setPropertyPath(propertyPath.toString()).setMessage(message).setInvalidValue(invalidValue);
  }

}