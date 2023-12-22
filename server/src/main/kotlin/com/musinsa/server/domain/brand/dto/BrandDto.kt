package com.musinsa.server.domain.brand.dto

import com.musinsa.server.infra.database.brand.entity.Brand

data class BrandDto(
    val brandId: Long,
    val brandName: String,
) {
    companion object {
        fun of(brand: Brand): BrandDto {
            return BrandDto(
                brandId = brand.brandId!!,
                brandName = brand.brandName,
            )
        }
    }
}
