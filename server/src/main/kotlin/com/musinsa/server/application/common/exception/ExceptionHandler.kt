package com.musinsa.server.application.common.exception

import com.musinsa.server.common.helper.logger
import com.musinsa.server.application.common.response.Response
import com.musinsa.server.application.common.response.ResponseCode
import com.musinsa.server.common.exception.BizException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): Response<Any?> {
        logger.error("exceptionHandler: ", e)
        val message = e.message ?: ResponseCode.ERROR_SERVER.message
        return Response.error(ResponseCode.ERROR_SERVER.code, message)
    }

    @ExceptionHandler(BizException::class)
    fun bizExceptionHandler(e: BizException): Response<Any?> {
        logger.error("bizExceptionHandler: ", e)
        return Response.error(e.code)
    }
}
