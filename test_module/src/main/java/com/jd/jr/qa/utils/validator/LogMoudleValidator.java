package com.jd.jr.qa.utils.validator;

import com.jd.jr.qa.constants.enums.ResponseEnum;
import com.jd.jr.qa.entity.LogRequestEntity;
import com.jd.jr.qa.entity.MethodMockRequestEntity;
import org.springframework.stereotype.Component;

import static com.jd.jr.qa.utils.ValidatorUtil.isBlank;
import static com.jd.jr.qa.utils.ValidatorUtil.isNull;

/**
 * Created by Gochin on 2021/7/26.
 */
@Component("logMoudleValidator")
public class LogMoudleValidator extends BaseValidator<LogRequestEntity>{


    @Override
    public void validate(LogRequestEntity entity) throws Exception {
        isNull(entity, ResponseEnum.ARG_IS_NULL_ERROR, "entity不能为空");
        isBlank(entity.getMethodName(), ResponseEnum.ARG_IS_NULL_ERROR, "需要动态增加日志的方法名不能为空");
        isBlank(entity.getClassName(), ResponseEnum.ARG_IS_NULL_ERROR, "类的绝对路径不能为空");
    }
}
