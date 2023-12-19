package com.musinsa.server.infra.database.category.entity

import com.musinsa.server.infra.database.common.entity.AuditingEntity
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "category")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categoryId")
    var categoryId: Long? = null,

    @Column(name = "categoryName")
    var categoryName: String,

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
