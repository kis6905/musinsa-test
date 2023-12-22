package com.musinsa.server.infra.database.common.entity

import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditingEntity(
    @Column(name = "createdBy")
    var createdBy: String,

    @CreationTimestamp
    @CreatedDate
    @Column(name = "createdAt")
    var createdAt: LocalDateTime? = null,

    @Column(name = "modifiedBy")
    var modifiedBy: String? = null,

    @CreationTimestamp
    @LastModifiedDate
    @Column(name = "modifiedAt")
    var modifiedAt: LocalDateTime? = null,
) {
    companion object {
        const val SYSTEM: String = "SYSTEM"
        const val ADMIN: String = "ADMIN"
    }
}
