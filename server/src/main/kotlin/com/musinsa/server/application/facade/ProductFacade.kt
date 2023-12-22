package com.musinsa.server.application.facade

import com.musinsa.server.common.exception.BizException
import com.musinsa.server.application.common.response.ResponseCode
import com.musinsa.server.application.facade.dto.BrandProductsByCategoryDto
import com.musinsa.server.application.facade.dto.HighestLowestCategoryProduct
import com.musinsa.server.application.facade.dto.LowestPriceDto
import com.musinsa.server.application.facade.dto.ProductsAndTotalPriceDto
import com.musinsa.server.domain.brand.BrandService
import com.musinsa.server.domain.category.CategoryService
import com.musinsa.server.domain.category.dto.CategoryDto
import com.musinsa.server.domain.product.ProductService
import com.musinsa.server.domain.product.dto.ProductDto
import com.musinsa.server.infra.database.common.entity.AuditingEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductFacade(
    val categoryService: CategoryService,
    val productService: ProductService,
    val brandService: BrandService,
) {

    fun findLowestPriceProductsByCategories(): ProductsAndTotalPriceDto {
        // 모든 카테고리 조회
        val categoryList = categoryService.findAllCategories()

        // 카테고리 별 최저가 상품 조회
        val productList = mutableListOf<ProductDto>()
        var totalPrice = 0L
        categoryList.forEach {
            productService.findLowestPriceProductByCategory(it.categoryId)?.apply {
                productList.add(this)
                totalPrice += this.price
            }
        }

        return ProductsAndTotalPriceDto(
            productList = productList,
            totalPrice = totalPrice,
        )
    }

    fun findProductsOfLowestPriceBrand(): LowestPriceDto {
        // 모든 브랜드 조회
        val brandList = brandService.findAllBrands()
        if (brandList.isEmpty()) {
            throw BizException(ResponseCode.NOT_FOUND_BRAND)
        }

        // 상품 합계가 가장 낮은 브랜드 추출
        val lowestPriceBrand: BrandProductsByCategoryDto = brandList
            .map {
                val productList = productService.findProductListByBrand(it.brandId)
                val sumPrice: Long = productList.sumOf { product -> product.price }
                BrandProductsByCategoryDto(
                    brandId = it.brandId,
                    brandName = it.brandName,
                    productsByCategory = productList,
                    totalPrice = sumPrice,
                )
            }
            .filterNot { it.productsByCategory.isEmpty() }
            .minBy { it.totalPrice }

        return LowestPriceDto(
            lowestPriceBrand = lowestPriceBrand
        )
    }

    fun findHighestAndLowestPriceProductsByCategory(categoryId: Long): HighestLowestCategoryProduct {
        val category: CategoryDto = categoryService.findById(categoryId) ?: throw BizException(ResponseCode.NOT_FOUND_CATEGORY)
        val lowestPriceProduct = productService.findLowestPriceProductByCategory(categoryId)
        val highestPriceProduct = productService.findHighestPriceProductByCategory(categoryId)

        return HighestLowestCategoryProduct(
            categoryId = category.categoryId,
            categoryName = category.categoryName,
            lowestPriceProduct = lowestPriceProduct,
            highestPriceProduct = highestPriceProduct,
        )
    }

    fun findAllProducts(): List<ProductDto> {
        return productService.findAllProducts()
    }

    @Transactional(readOnly = false)
    fun saveProduct(productDto: ProductDto): ProductDto {
        val saved = productService.saveProduct(productDto, AuditingEntity.ADMIN)
        evictCache(saved)
        return saved
    }

    fun modifyProduct(productDto: ProductDto): ProductDto {
        val saved = productService.modifyProduct(productDto, AuditingEntity.ADMIN)
        evictCache(saved)
        return saved
    }

    @Transactional(readOnly = false)
    fun removeProduct(productId: Long) {
        val productDto = productService.findById(productId) ?: throw BizException(ResponseCode.NOT_FOUND_PRODUCT)
        productService.removeProduct(productId, AuditingEntity.ADMIN)
        evictCache(productDto)
    }

    private fun evictCache(productDto: ProductDto) {
        productService.evictProductListByBrand(productDto.brandId!!)
        productService.evictLowestPriceProductByCategory(productDto.categoryId!!)
        productService.evictHighestPriceProductByCategory(productDto.categoryId)
    }

}
