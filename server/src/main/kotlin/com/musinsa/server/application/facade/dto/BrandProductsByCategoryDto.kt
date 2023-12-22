package com.musinsa.server.application.facade.dto

import com.musinsa.server.domain.product.dto.ProductDto

data class BrandProductsByCategoryDto(
    val brandId: Long,
    val brandName: String,
    val productsByCategory: List<ProductDto>,
    val totalPrice: Long,
)