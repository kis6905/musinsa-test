package com.musinsa.server.common.exception

import com.musinsa.server.application.common.response.ResponseCode

class BizException(
    val code: ResponseCode = ResponseCode.ERROR_SERVER,
): RuntimeException(code.message)
