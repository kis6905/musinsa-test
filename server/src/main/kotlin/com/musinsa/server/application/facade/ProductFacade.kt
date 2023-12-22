package com.musinsa.server.application.facade

import com.musinsa.server.application.common.exception.BizException
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
import org.springframework.stereotype.Service

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
            .minBy { it.totalPrice }

        return LowestPriceDto(
            lowestPriceBrand = lowestPriceBrand
        )
    }

    fun findHighestAndLowestPriceProductsByCategory(categoryId: Long): HighestLowestCategoryProduct {
        val category: CategoryDto = categoryService.findById(categoryId) ?: throw BizException(ResponseCode.NOT_FOUND_BRAND)
        val lowestPriceProduct = productService.findLowestPriceProductByCategory(categoryId)
        val highestPriceProduct = productService.findHighestPriceProductByCategory(categoryId)

        return HighestLowestCategoryProduct(
            categoryId = category.categoryId,
            categoryName = category.categoryName,
            lowestPriceProduct = lowestPriceProduct,
            highestPriceProduct = highestPriceProduct,
        )
    }

}
