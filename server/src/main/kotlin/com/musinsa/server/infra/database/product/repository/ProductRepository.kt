package com.musinsa.server.infra.database.product.repository

import com.musinsa.server.infra.database.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun findTopByCategoryCategoryIdAndDeletedFalseOrderByPriceAscCategoryCategoryNameAsc(categoryId: Long): Product?
    fun findTopByCategoryCategoryIdAndDeletedFalseOrderByPriceDescCategoryCategoryNameAsc(categoryId: Long): Product?
    fun findAllByBrandBrandIdAndDeletedFalse(brandId: Long): List<Product>
    fun findAllByDeletedFalse(): List<Product>
}
