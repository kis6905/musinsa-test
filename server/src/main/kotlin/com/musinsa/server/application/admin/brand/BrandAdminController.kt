package com.musinsa.server.application.admin.brand

import com.musinsa.server.application.common.annotations.AdminController
import com.musinsa.server.application.common.response.Response
import com.musinsa.server.application.facade.BrandFacade
import com.musinsa.server.domain.brand.dto.BrandDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@AdminController
class BrandAdminController(
    val brandFacade: BrandFacade,
) {

    // 구현 4) 브랜드 추가 / 업데이트 / 삭제 API

    @GetMapping("/brands")
    fun getBrands(): Response<List<BrandDto>> {
        return Response.ok(brandFacade.findAllBrands())
    }

    @PostMapping("/brand")
    fun postBrand(@RequestBody brandDto: BrandDto): Response<BrandDto> {
        return Response.ok(brandFacade.saveBrand(brandDto))
    }

    @PutMapping("/brand")
    fun putBrand(@RequestBody brandDto: BrandDto): Response<BrandDto> {
        return Response.ok(brandFacade.modifyBrand(brandDto))
    }

    @DeleteMapping("/brand/{brandId}")
    fun deleteBrand(@PathVariable brandId: Long): Response<Any?> {
        brandFacade.removeBrand(brandId)
        return Response.ok()
    }

}
