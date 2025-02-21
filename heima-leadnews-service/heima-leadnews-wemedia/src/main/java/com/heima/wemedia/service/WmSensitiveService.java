package com.heima.wemedia.service;

import com.heima.model.common.dtos.ResponseResult;

public interface WmSensitiveService {
    public ResponseResult checkSensitive(String content);
}
