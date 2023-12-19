package com.musinsa.server.infra.database

import com.musinsa.server.infra.database.brand.entity.Brand
import com.musinsa.server.infra.database.brand.repository.BrandRepository
import com.musinsa.server.infra.database.category.entity.Category
import com.musinsa.server.infra.database.category.repository.CategoryRepository
import com.musinsa.server.infra.database.common.entity.AuditingEntity
import com.musinsa.server.infra.database.product.entity.Product
import com.musinsa.server.infra.database.product.repository.ProductRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class DataInit(
    val brandRepository: BrandRepository,
    val categoryRepository: CategoryRepository,
    val productRepository: ProductRepository,
) {

    companion object {
        val PRICE_MAP = mapOf(
            "A" to listOf(11_200L, 5_500L, 4_200L, 9_000L, 2_000L, 1_700L, 1_800L, 2_300L),
            "B" to listOf(10_500L, 5_900L, 3_800L, 9_100L, 2_100L, 2_000L, 2_000L, 2_200L),
            "C" to listOf(10_000L, 6_200L, 3_300L, 9_200L, 2_200L, 1_900L, 2_200L, 2_100L),
            "D" to listOf(10_100L, 5_100L, 3_000L, 9_500L, 2_500L, 1_500L, 2_400L, 2_000L),
            "E" to listOf(10_700L, 5_000L, 3_800L, 9_900L, 2_300L, 1_800L, 2_100L, 2_100L),
            "F" to listOf(11_200L, 7_200L, 4_000L, 9_300L, 2_100L, 1_600L, 2_300L, 1_900L),
            "G" to listOf(10_500L, 5_800L, 3_900L, 9_000L, 2_200L, 1_700L, 2_100L, 2_000L),
            "H" to listOf(10_800L, 6_300L, 3_100L, 9_700L, 2_100L, 1_600L, 2_000L, 2_000L),
            "I" to listOf(11_400L, 6_700L, 3_200L, 9_500L, 2_400L, 1_700L, 1_700L, 2_400L),
        )
    }

    @PostConstruct
    @Transactional(readOnly = false)
    fun init() {
        val brandList = listOf(
            Brand(brandName = "A", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "B", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "C", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "D", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "E", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "F", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "G", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "H", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Brand(brandName = "I", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
        )
        val savedBrandList = brandRepository.saveAll(brandList)
        brandRepository.flush()

        val categoryList = listOf(
            Category(categoryName = "상의", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Category(categoryName = "아우터", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Category(categoryName = "바지", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Category(categoryName = "스니커즈", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Category(categoryName = "가방", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Category(categoryName = "모자", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Category(categoryName = "양말", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
            Category(categoryName = "액세서리", _createdBy = AuditingEntity.SYSTEM, _modifiedBy = AuditingEntity.SYSTEM),
        )
        val savedCategoryList = categoryRepository.saveAll(categoryList)
        categoryRepository.flush()

        val productList: List<Product> = savedBrandList
            .flatMap { brand ->
                PRICE_MAP[brand.brandName]!!.mapIndexed { idx, price ->
                    val category = savedCategoryList[idx]
                    Product(
                        price = price,
                        brand = brand,
                        category = category,
                        _createdBy = AuditingEntity.SYSTEM,
                        _modifiedBy = AuditingEntity.SYSTEM
                    )
                }
            }
        productRepository.saveAll(productList)
    }

}
