package com.musinsa.server.domain.brand

import com.musinsa.server.application.common.response.ResponseCode
import com.musinsa.server.common.exception.BizException
import com.musinsa.server.common.helper.logger
import com.musinsa.server.domain.brand.dto.BrandDto
import com.musinsa.server.infra.database.brand.entity.Brand
import com.musinsa.server.infra.database.brand.repository.BrandRepository
import com.musinsa.server.infra.database.common.entity.AuditingEntity
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class BrandService(
    val brandRepository: BrandRepository,
) {

    companion object {
        const val CACHE_NAME_ALL_BRANDS = "allBrands"
    }

    @Cacheable(value = [CACHE_NAME_ALL_BRANDS])
    @Transactional(readOnly = true)
    fun findAllBrands(): List<BrandDto> {
        val brandList = brandRepository.findAllByDeletedFalse()
        return brandList.map { BrandDto.of(it) }
    }

    @CacheEvict(value = [CACHE_NAME_ALL_BRANDS])
    fun evictAllBrands() {
        logger.info("Evict cache: $CACHE_NAME_ALL_BRANDS")
    }

    @Transactional(readOnly = false)
    fun saveBrand(brandDto: BrandDto, userId: String): BrandDto {
        val entity = Brand.ofForSaving(brandDto, userId)
        val saved = brandRepository.save(entity)
        return BrandDto.of(saved)
    }

    @Transactional(readOnly = false)
    fun modifyBrand(brandDto: BrandDto, userId: String): BrandDto {
        val entity = brandRepository.findById(brandDto.brandId).getOrNull() ?: throw BizException(ResponseCode.NOT_FOUND_BRAND)
        entity.update(brandDto, userId)
        val saved = brandRepository.save(entity)
        return BrandDto.of(saved)
    }

    @Transactional(readOnly = false)
    fun removeBrand(brandId: Long, userId: String) {
        val entity = brandRepository.findById(brandId).getOrNull() ?: throw BizException(ResponseCode.NOT_FOUND_BRAND)
        entity.remove(userId)
        brandRepository.save(entity)
    }
}
