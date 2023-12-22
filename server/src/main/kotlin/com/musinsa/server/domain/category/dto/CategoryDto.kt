package com.musinsa.server.domain.category.dto

import com.musinsa.server.infra.database.category.entity.Category

data class CategoryDto(
    var categoryId: Long,
    var categoryName: String,
) {
    companion object {
        fun of(category: Category): CategoryDto {
            return CategoryDto(
                categoryId = category.categoryId!!,
                categoryName = category.categoryName,
            )
        }
    }
}
