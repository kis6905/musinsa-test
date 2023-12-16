package com.musinsa.server.domain.product.dto

data class ProductDto(
    val productId: Long,
    val price: Long,
    val brandId: Long?,
    val brandName: String?,
    val categoryId: Long?,
    val categoryName: String?
)
