package com.musinsa.server.application.api.product

import com.musinsa.server.domain.product.ProductService
import org.springframework.stereotype.Service

@Service
class ProductApiFacade(
    val productService: ProductService
) {

    fun findLowPriceProductsByCategories() {
        // 모든 category 조회
        // val categories: List<CategoryDto> = ...

        // category 별 최저가 상품 가격 + 브랜드 이름
//        productService.findLowestPriceProductByCategory()
    }
}
