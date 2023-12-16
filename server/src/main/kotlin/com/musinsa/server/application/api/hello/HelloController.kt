package com.musinsa.server.application.api.hello

import com.musinsa.server.application.common.annotations.ApiController
import org.springframework.web.bind.annotation.GetMapping

@ApiController
class HelloController {

    @GetMapping
    fun hello(): String {
        return "ok"
    }
}
