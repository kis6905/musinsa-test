package com.musinsa.server.domain.brand

import com.musinsa.server.common.helper.logger
import com.musinsa.server.domain.brand.dto.BrandDto
import com.musinsa.server.infra.database.brand.repository.BrandRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class BrandService(
    val brandRepository: BrandRepository,
) {

    companion object {
        const val CACHE_NAME_ALL_BRANDS = "allBrands"
    }

    @Cacheable(value = [CACHE_NAME_ALL_BRANDS])
    fun findAllBrands(): List<BrandDto> {
        val brandList = brandRepository.findAllByDeletedFalse()
        return brandList.map { BrandDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_ALL_BRANDS])
    fun evictAllBrands() {
        logger.info("Evict cache: $CACHE_NAME_ALL_BRANDS")
    }
}
