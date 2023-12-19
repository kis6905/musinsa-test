package com.musinsa.server.infra.database.category.repository

import com.musinsa.server.infra.database.category.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<Category, Long> {
}
