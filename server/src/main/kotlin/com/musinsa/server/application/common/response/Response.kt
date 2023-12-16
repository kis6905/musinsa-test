package com.musinsa.server.application.common.response

class Response<T> private constructor(
    val code: String,
    val message: String,
    val body: T? = null) {

    companion object {
        fun <T> ok(body: T? = null): Response<T> {
            return Response(
                code = ResponseCode.OK.code,
                message = ResponseCode.OK.message,
                body = body,
            )
        }

        fun <T> error(responseCode: ResponseCode = ResponseCode.ERROR_SERVER): Response<T> {
            return Response(
                code = responseCode.code,
                message = responseCode.message,
            )
        }

        fun <T> error(
            code: String = ResponseCode.ERROR_SERVER.code,
            message: String = ResponseCode.ERROR_SERVER.message): Response<T> {
            return Response(
                code = code,
                message = message,
            )
        }
    }

}
