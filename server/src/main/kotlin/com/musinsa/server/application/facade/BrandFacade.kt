package com.musinsa.server.application.facade

import com.musinsa.server.domain.brand.BrandService
import com.musinsa.server.domain.brand.dto.BrandDto
import com.musinsa.server.domain.category.CategoryService
import com.musinsa.server.domain.product.ProductService
import com.musinsa.server.infra.database.common.entity.AuditingEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandFacade(
    val brandService: BrandService,
    val productService: ProductService,
    val categoryService: CategoryService,
) {

    fun findAllBrands(): List<BrandDto> {
        return brandService.findAllBrands()
    }

    fun saveBrand(brandDto: BrandDto): BrandDto {
        val saved = brandService.saveBrand(brandDto, AuditingEntity.ADMIN)
        brandService.evictAllBrands()
        return saved
    }

    fun modifyBrand(brandDto: BrandDto): BrandDto {
        val saved = brandService.modifyBrand(brandDto, AuditingEntity.ADMIN)
        evictCache(saved.brandId)
        return saved
    }

    @Transactional(readOnly = false)
    fun removeBrand(brandId: Long) {
        brandService.removeBrand(brandId, AuditingEntity.ADMIN)
        productService.removeByBrand(brandId, AuditingEntity.ADMIN)
        evictCache(brandId)
    }

    private fun evictCache(brandId: Long) {
        brandService.evictAllBrands()
        productService.evictProductListByBrand(brandId)
        categoryService.findAllCategories().forEach {
            productService.evictLowestPriceProductByCategory(it.categoryId)
            productService.evictHighestPriceProductByCategory(it.categoryId)
        }
    }
}
