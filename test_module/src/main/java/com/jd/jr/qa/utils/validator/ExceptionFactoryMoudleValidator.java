package com.jd.jr.qa.utils.validator;

import com.jd.jr.qa.constants.enums.ResponseEnum;
import com.jd.jr.qa.entity.ExceptionFactoryRequestEntity;
import com.jd.jr.qa.entity.LogRequestEntity;
import org.springframework.stereotype.Component;

import static com.jd.jr.qa.utils.ValidatorUtil.isBlank;
import static com.jd.jr.qa.utils.ValidatorUtil.isNull;

/**
 * Created by Gochin on 2021/7/26.
 */
@Component("exceptionFactoryRequestEntity")
public class ExceptionFactoryMoudleValidator extends BaseValidator<ExceptionFactoryRequestEntity>{



    @Override
    public void validate(ExceptionFactoryRequestEntity entity) throws Exception {
        isNull(entity, ResponseEnum.ARG_IS_NULL_ERROR, "entity不能为空");
        isBlank(entity.getMethodName(), ResponseEnum.ARG_IS_NULL_ERROR, "方法名不能为空");
        isBlank(entity.getClassName(), ResponseEnum.ARG_IS_NULL_ERROR, "类的绝对路径不能为空");
        isBlank(entity.getExceptionType(), ResponseEnum.ARG_IS_NULL_ERROR, "需要抛出的异常类型不能为空");
    }
}
