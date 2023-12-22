package com.musinsa.server.application.common.response

enum class ResponseCode(val code: String, val message: String) {
    OK("0000", "정상입니다."),
    ERROR_SERVER("0001", "서버 에러가 발생했습니다. 다시 시도해주세요."),
    NOT_FOUND_BRAND("0002", "브랜드를 찾을수 없습니다."),
    NOT_FOUND_CATEGORY("0002", "카테고리를 찾을수 없습니다."),
}
