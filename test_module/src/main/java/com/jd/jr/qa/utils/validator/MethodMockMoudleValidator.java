package com.jd.jr.qa.utils.validator;

import com.jd.jr.qa.constants.enums.ResponseEnum;
import com.jd.jr.qa.entity.BaseRequestEntity;
import com.jd.jr.qa.entity.MethodMockRequestEntity;
import org.springframework.stereotype.Component;

import static com.jd.jr.qa.utils.ValidatorUtil.*;

/**
 * Created by Gochin on 2021/7/26.
 */
@Component("methodMockMoudleValidator")
public class MethodMockMoudleValidator extends BaseValidator<MethodMockRequestEntity>{


    @Override
    public void validate(MethodMockRequestEntity entity) throws Exception {
        isNull(entity, ResponseEnum.ARG_IS_NULL_ERROR, "entity不能为空");
        isBlank(entity.getNameSpace(), ResponseEnum.ARG_IS_NULL_ERROR, "SandBox的命名空间不能为空");
        isBlank(entity.getMethodName(), ResponseEnum.ARG_IS_NULL_ERROR, "被mock的方法名不能为空");
        isBlank(entity.getClassName(), ResponseEnum.ARG_IS_NULL_ERROR, "类的绝对路径不能为空");
        isBlank(entity.getResponseString(), ResponseEnum.ARG_IS_NULL_ERROR, "mock出参不能为空");
    }
}
