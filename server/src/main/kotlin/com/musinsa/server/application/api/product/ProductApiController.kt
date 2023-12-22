package com.musinsa.server.application.api.product

import com.musinsa.server.application.facade.dto.ProductsAndTotalPriceDto
import com.musinsa.server.application.common.annotations.ApiController
import com.musinsa.server.application.common.response.Response
import com.musinsa.server.application.facade.ProductFacade
import com.musinsa.server.application.facade.dto.HighestLowestCategoryProduct
import com.musinsa.server.application.facade.dto.LowestPriceDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@ApiController
class ProductApiController(
    val productFacade: ProductFacade,
) {

    // 구현 1) 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
    @GetMapping("/product/low/price/products/by/categories")
    fun getProductLowPriceProductsByCategories(): Response<ProductsAndTotalPriceDto> {
        return Response.ok(productFacade.findLowestPriceProductsByCategories())
    }

    // 구현 2) 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
    @GetMapping("/product/low/brand/products")
    fun getProductLowBrandProducts(): Response<LowestPriceDto> {
        return Response.ok(productFacade.findProductsOfLowestPriceBrand())
    }

    // 구현 3) 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
    @GetMapping("/product/low-high/brand/product/by/{categoryId}")
    fun getProductLowHighBrandProducts(@PathVariable categoryId: Long): Response<HighestLowestCategoryProduct> {
        return Response.ok(productFacade.findHighestAndLowestPriceProductsByCategory(categoryId))
    }
}
