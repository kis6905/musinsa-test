package com.musinsa.server.application.admin.product

import com.musinsa.server.application.common.annotations.AdminController
import com.musinsa.server.application.common.response.Response
import com.musinsa.server.application.facade.ProductFacade
import com.musinsa.server.domain.product.dto.ProductDto
import org.springframework.web.bind.annotation.*

@AdminController
class ProductAdminController(
    val productFacade: ProductFacade,
) {

    // // 구현 4) 상품 추가 / 업데이트 / 삭제 API

    @GetMapping("/products")
    fun getProducts(): Response<List<ProductDto>> {
        return Response.ok(productFacade.findAllProducts())
    }

    @PostMapping("/product")
    fun postProduct(@RequestBody productDto: ProductDto): Response<ProductDto> {
        return Response.ok(productFacade.saveProduct(productDto))
    }

    @PutMapping("/product")
    fun putProduct(@RequestBody productDto: ProductDto): Response<ProductDto> {
        return Response.ok(productFacade.modifyProduct(productDto))
    }

    @DeleteMapping("/product/{productId}")
    fun deleteProduct(@PathVariable productId: Long): Response<Any?> {
        productFacade.removeProduct(productId)
        return Response.ok()
    }
}