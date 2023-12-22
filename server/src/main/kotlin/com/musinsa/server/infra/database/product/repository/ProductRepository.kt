package com.musinsa.server.infra.database.product.repository

import com.musinsa.server.infra.database.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun findTopByCategoryCategoryIdOrderByPriceAscCategoryCategoryNameAsc(categoryId: Long): Product?
    fun findTopByCategoryCategoryIdOrderByPriceDescCategoryCategoryNameAsc(categoryId: Long): Product?
    fun findAllByBrandBrandId(brandId: Long): List<Product>
}
