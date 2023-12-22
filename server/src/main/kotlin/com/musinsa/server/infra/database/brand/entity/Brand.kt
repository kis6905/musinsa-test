package com.musinsa.server.infra.database.brand.entity

import com.musinsa.server.domain.brand.dto.BrandDto
import com.musinsa.server.infra.database.common.entity.AuditingEntity
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "brand")
class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "brandId")
    var brandId: Long? = null,

    @Column(name = "brandName")
    var brandName: String,

    @Column(name = "deleted")
    var deleted: Boolean = false,

    @Transient
    var _createdBy: String,
    @Transient
    var _modifiedBy: String,
): AuditingEntity(
    createdBy = _createdBy,
    modifiedBy = _modifiedBy,
) {
    companion object {
        fun ofForSaving(dto: BrandDto, createdBy: String): Brand {
            return Brand(
                brandName = dto.brandName,
                _createdBy = createdBy,
                _modifiedBy = createdBy,
            )
        }
    }

    fun update(dto: BrandDto, modifiedBy: String) {
        this.brandName = dto.brandName
        this._modifiedBy = modifiedBy
    }

    fun remove(modifiedBy: String) {
        this.deleted = true
        this._modifiedBy = modifiedBy
    }
}
