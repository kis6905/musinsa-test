package com.musinsa.server.infra.database.brand.repository

import com.musinsa.server.infra.database.brand.entity.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository: JpaRepository<Brand, Long> {
    fun findAllByDeletedFalse(): List<Brand>
}
