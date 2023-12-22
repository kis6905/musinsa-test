package com.musinsa.server.application.facade.dto

import com.musinsa.server.domain.product.dto.ProductDto

data class ProductsAndTotalPriceDto(
    val productList: List<ProductDto>,
    val totalPrice: Long,
)
