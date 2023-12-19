package com.musinsa.server.infra.database.product.entity

import com.musinsa.server.infra.database.brand.entity.Brand
import com.musinsa.server.infra.database.category.entity.Category
import com.musinsa.server.infra.database.common.entity.AuditingEntity
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "productId")
    var productId: Long? = null,

    @Column(name = "price")
    var price: Long,

    @Column(name = "deleted")
    var deleted: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId")
    var brand: Brand,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    var category: Category,

    @Transient
    var _createdBy: String,
    @Transient
    var _modifiedBy: String,
): AuditingEntity(
    createdBy = _createdBy,
    modifiedBy = _modifiedBy,
)
