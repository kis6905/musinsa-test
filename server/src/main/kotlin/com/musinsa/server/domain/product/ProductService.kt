package com.musinsa.server.domain.product

import com.musinsa.server.application.common.response.ResponseCode
import com.musinsa.server.common.exception.BizException
import com.musinsa.server.common.helper.logger
import com.musinsa.server.domain.product.dto.ProductDto
import com.musinsa.server.infra.database.brand.repository.BrandRepository
import com.musinsa.server.infra.database.category.repository.CategoryRepository
import com.musinsa.server.infra.database.product.entity.Product
import com.musinsa.server.infra.database.product.repository.ProductRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(
    val productRepository: ProductRepository,
    val brandRepository: BrandRepository,
    val categoryRepository: CategoryRepository,
) {

    companion object {
        const val CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY = "lowestPriceProductByCategory"
        const val CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY = "highestPriceProductByCategory"
        const val CACHE_NAME_PRODUCT_LIST_BY_BRAND = "productListByBrand"
    }

    @Cacheable(value = [CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", unless = "#result == null")
    @Transactional(readOnly = true)
    fun findLowestPriceProductByCategory(categoryId: Long): ProductDto? {
        val product: Product? = productRepository.findTopByCategoryCategoryIdAndDeletedFalseOrderByPriceAscCategoryCategoryNameAsc(categoryId)
        return product?.let { ProductDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", allEntries = false)
    fun evictLowestPriceProductByCategory(categoryId: Long) {
        logger.info("Evict cache: $CACHE_NAME_LOWEST_PRICE_PRODUCT_BY_CATEGORY, categoryId: $categoryId")
    }

    @Cacheable(value = [CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", unless = "#result == null")
    @Transactional(readOnly = true)
    fun findHighestPriceProductByCategory(categoryId: Long): ProductDto? {
        val product: Product? = productRepository.findTopByCategoryCategoryIdAndDeletedFalseOrderByPriceDescCategoryCategoryNameAsc(categoryId)
        return product?.let { ProductDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY], key = "#categoryId", allEntries = false)
    fun evictHighestPriceProductByCategory(categoryId: Long) {
        logger.info("Evict cache: $CACHE_NAME_HIGHEST_PRICE_PRODUCT_BY_CATEGORY, categoryId: $categoryId")
    }

    @Cacheable(value = [CACHE_NAME_PRODUCT_LIST_BY_BRAND], key = "#brandId", unless = "#result == null")
    @Transactional(readOnly = true)
    fun findProductListByBrand(brandId: Long): List<ProductDto> {
        val productList: List<Product> = productRepository.findAllByBrandBrandIdAndDeletedFalse(brandId)
        return productList.map { ProductDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_PRODUCT_LIST_BY_BRAND], key = "#brandId", allEntries = false)
    fun evictProductListByBrand(brandId: Long) {
        logger.info("Evict cache: $CACHE_NAME_PRODUCT_LIST_BY_BRAND, brandId: $brandId")
    }

    @Transactional(readOnly = false)
    fun removeByBrand(brandId: Long, userId: String) {
        val productList = productRepository.findAllByBrandBrandIdAndDeletedFalse(brandId)
        productList.map {
            it.deleted = true
            it.modifiedBy = userId
        }
        productRepository.saveAll(productList)
    }

    @Transactional(readOnly = true)
    fun findAllProducts(): List<ProductDto> {
        return productRepository.findAllByDeletedFalse().map { ProductDto.of(it) }
    }

    @Transactional(readOnly = true)
    fun findById(productId: Long): ProductDto? {
        return  productRepository.findById(productId).getOrNull()?.let { ProductDto.of(it) }
    }

    @Transactional(readOnly = false)
    fun saveProduct(productDto: ProductDto, userId: String): ProductDto {
        val brand = brandRepository.findById(productDto.brandId!!).getOrNull() ?: throw BizException(ResponseCode.NOT_FOUND_BRAND)
        val category = categoryRepository.findById(productDto.categoryId!!).getOrNull() ?: throw BizException(ResponseCode.NOT_FOUND_CATEGORY)

        val entity = Product.ofForSaving(productDto, brand, category, userId)

        val saved = productRepository.save(entity)
        return ProductDto.of(saved)
    }

    @Transactional(readOnly = false)
    fun modifyProduct(productDto: ProductDto, userId: String): ProductDto {
        val entity = productRepository.findById(productDto.productId).getOrNull() ?: throw BizException(ResponseCode.NOT_FOUND_PRODUCT)
        entity.update(productDto, userId)
        val saved = productRepository.save(entity)
        return ProductDto.of(saved)
    }

    @Transactional(readOnly = false)
    fun removeProduct(productId: Long, userId: String) {
        val entity = productRepository.findById(productId).getOrNull() ?: throw BizException(ResponseCode.NOT_FOUND_PRODUCT)
        entity.remove(userId)
        productRepository.save(entity)
    }

}
