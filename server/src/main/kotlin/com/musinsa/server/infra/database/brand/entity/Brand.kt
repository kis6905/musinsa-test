package com.musinsa.server.infra.database.brand.entity

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
)
