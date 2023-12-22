package com.musinsa.server.domain.product.dto

import com.musinsa.server.infra.database.product.entity.Product

data class ProductDto(
    val productId: Long,
    val price: Long,
    val brandId: Long? = null,
    val brandName: String? = null,
    val categoryId: Long? = null,
    val categoryName: String? = null,
) {
    companion object {
        fun of(product: Product): ProductDto {
            return ProductDto(
                productId = product.productId!!,
                price = product.price,
                brandId = product.brand?.brandId,
                brandName = product.brand?.brandName,
                categoryId = product.category?.categoryId,
                categoryName = product.category?.categoryName,
            )
        }
    }
}
