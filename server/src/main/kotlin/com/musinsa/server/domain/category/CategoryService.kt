package com.musinsa.server.domain.category

import com.musinsa.server.application.common.helper.logger
import com.musinsa.server.domain.category.dto.CategoryDto
import com.musinsa.server.infra.database.category.repository.CategoryRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class CategoryService(
    val categoryRepository: CategoryRepository,
) {

    companion object {
        const val CACHE_NAME_ALL_CATEGORIES = "allCategories"
        const val CACHE_NAME_CATEGORY = "category"
    }

    @Cacheable(value = [CACHE_NAME_ALL_CATEGORIES])
    @Transactional(readOnly = true)
    fun findAllCategories(): List<CategoryDto> {
        val categoryList = categoryRepository.findAllByDeletedFalse()
        return categoryList.map { CategoryDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_ALL_CATEGORIES])
    fun evictAllCategories() {
        logger.info("Evict cache: $CACHE_NAME_ALL_CATEGORIES")
    }

    @Cacheable(value = [CACHE_NAME_CATEGORY], key = "#categoryId", unless = "#result == null")
    @Transactional(readOnly = true)
    fun findById(categoryId: Long): CategoryDto? {
        return categoryRepository.findById(categoryId).getOrNull()?.let { CategoryDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_CATEGORY])
    fun evictCategory(categoryId: Long) {
        logger.info("Evict cache: $CACHE_NAME_CATEGORY, categoryId: $categoryId")
    }
}
