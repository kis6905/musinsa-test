package com.musinsa.server.application.facade.dto

import com.musinsa.server.domain.product.dto.ProductDto

data class HighestLowestCategoryProduct(
    val categoryId: Long,
    val categoryName: String,
    val lowestPriceProduct: ProductDto?,
    val highestPriceProduct: ProductDto?,
)
