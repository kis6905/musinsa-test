package com.musinsa.server.domain.product

import com.musinsa.server.common.helper.logger
import com.musinsa.server.domain.product.dto.ProductDto
import com.musinsa.server.infra.database.product.entity.Product
import com.musinsa.server.infra.database.product.repository.ProductRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    val productRepository: ProductRepository,
) {

    companion object {
        const val CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY = "lowestPriceProductByCategory"
        const val CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY = "highestPriceProductByCategory"
        const val CACHE_NAME_PRODUCT_LIST_BY_BRAND = "productListByBrand"
    }

    @Cacheable(value = [CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", unless = "#result == null")
    @Transactional(readOnly = true)
    fun findLowestPriceProductByCategory(categoryId: Long): ProductDto? {
        val product: Product? = productRepository.findTopByCategoryCategoryIdOrderByPriceAscCategoryCategoryNameAsc(categoryId)
        return product?.let { ProductDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", allEntries = false)
    fun evictLowestPriceProductByCategory(categoryId: Long) {
        logger.info("Evict cache: $CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY, categoryId: $categoryId")
    }

    @Cacheable(value = [CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", unless = "#result == null")
    @Transactional(readOnly = true)
    fun findHighestPriceProductByCategory(categoryId: Long): ProductDto? {
        val product: Product? = productRepository.findTopByCategoryCategoryIdOrderByPriceDescCategoryCategoryNameAsc(categoryId)
        return product?.let { ProductDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", allEntries = false)
    fun evictHighestPriceProductByCategory(categoryId: Long) {
        logger.info("Evict cache: $CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY, categoryId: $categoryId")
    }

    @Cacheable(value = [CACHE_NAME_PRODUCT_LIST_BY_BRAND], key = "#brandId", unless = "#result == null")
    @Transactional(readOnly = true)
    fun findProductListByBrand(brandId: Long): List<ProductDto> {
        val productList: List<Product> = productRepository.findAllByBrandBrandId(brandId)
        return productList.map { ProductDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_PRODUCT_LIST_BY_BRAND], key = "#brandId", allEntries = false)
    fun evictProductListByBrand(brandId: Long) {
        logger.info("Evict cache: $CACHE_NAME_PRODUCT_LIST_BY_BRAND, brandId: $brandId")
    }

}
