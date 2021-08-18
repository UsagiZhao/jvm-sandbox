package com.jd.jr.qa.utils.validator;

import com.jd.jr.qa.entity.BaseRequestEntity;

/**
 * Created by Gochin on 2021/7/26.
 */
public abstract class BaseValidator<R extends BaseRequestEntity> {

    public abstract void validate(R request) throws Exception;

}
