package com.musinsa.server.application.facade

import com.appmattus.kotlinfixture.kotlinFixture
import com.musinsa.server.application.common.response.ResponseCode
import com.musinsa.server.common.exception.BizException
import com.musinsa.server.domain.brand.BrandService
import com.musinsa.server.domain.brand.dto.BrandDto
import com.musinsa.server.domain.category.CategoryService
import com.musinsa.server.domain.category.dto.CategoryDto
import com.musinsa.server.domain.product.ProductService
import com.musinsa.server.domain.product.dto.ProductDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ProductFacadeTest: BehaviorSpec({

    val fixture = kotlinFixture()

    val categoryService: CategoryService = mockk()
    val productService: ProductService = mockk()
    val brandService: BrandService = mockk()

    val stub = ProductFacade(
        categoryService = categoryService,
        productService = productService,
        brandService = brandService,
    )

    val categoryList = listOf(
        CategoryDto(categoryId = 1L, categoryName = "상의"),
        CategoryDto(categoryId = 2L, categoryName = "바지"),
    )
    val brandList = listOf(
        BrandDto(brandId = 11L, brandName = "A"),
        BrandDto(brandId = 12L, brandName = "B"),
    )

    Given("findLowestPriceProductsByCategories") {
        val product1 = fixture<ProductDto> {
            property(ProductDto::price) { 1000L }
        }
        val product2 = fixture<ProductDto> {
            property(ProductDto::price) { 2000L }
        }

        When("when") {
            every { categoryService.findAllCategories() } answers { categoryList }
            every { productService.findLowestPriceProductByCategory(1L) } answers { product1 }
            every { productService.findLowestPriceProductByCategory(2L) } answers { product2 }

            val result = stub.findLowestPriceProductsByCategories()

            Then("then") {
                result.totalPrice shouldBe 3000L
                result.productList.size shouldBe 2
            }
        }
    }

    Given("findProductsOfLowestPriceBrand") {
        val productListByBrand11 = listOf(
            fixture<ProductDto> {
                property(ProductDto::price) { 1000L }
            },
            fixture<ProductDto> {
                property(ProductDto::price) { 2000L }
            },
        )
        val productListByBrand12 = listOf(
            fixture<ProductDto> {
                property(ProductDto::price) { 5000L }
            },
            fixture<ProductDto> {
                property(ProductDto::price) { 7000L }
            },
        )

        When("정상") {
            every { brandService.findAllBrands() } answers { brandList }
            every { productService.findProductListByBrand(11L) } answers { productListByBrand11 }
            every { productService.findProductListByBrand(12L) } answers { productListByBrand12 }

            val result = stub.findProductsOfLowestPriceBrand()

            Then("then") {
                result.lowestPriceBrand.brandId shouldBe 11L
                result.lowestPriceBrand.totalPrice shouldBe 3000L
            }
        }

        When("브랜드가 없을 때") {
            every { brandService.findAllBrands() } answers { emptyList() }

            val result = shouldThrow<BizException> {
                stub.findProductsOfLowestPriceBrand()
            }

            Then("then") {
                result.code shouldBe ResponseCode.NOT_FOUND_BRAND
            }
        }
    }

    Given("findHighestAndLowestPriceProductsByCategory") {
        val categoryId = 1L
        val category = CategoryDto(categoryId = categoryId, categoryName = "상의")
        val lowest = fixture<ProductDto> {
            property(ProductDto::price) { 1000L }
        }
        val highest = fixture<ProductDto> {
            property(ProductDto::price) { 5000L }
        }

        When("정상") {
            every { categoryService.findById(categoryId) } answers { category }
            every { productService.findLowestPriceProductByCategory(categoryId) } answers { lowest }
            every { productService.findHighestPriceProductByCategory(categoryId) } answers { highest }

            val result = stub.findHighestAndLowestPriceProductsByCategory(categoryId)

            Then("then") {
                result.categoryId shouldBe 1L
                result.categoryName shouldBe "상의"
                result.lowestPriceProduct!!.price shouldBe 1000L
                result.highestPriceProduct!!.price shouldBe 5000L
            }
        }

        When("카테고리가 없을 때") {
            every { categoryService.findById(categoryId) } answers { null }

            val result = shouldThrow<BizException> {
                stub.findHighestAndLowestPriceProductsByCategory(categoryId)
            }

            Then("then") {
                result.code shouldBe ResponseCode.NOT_FOUND_CATEGORY
            }
        }
    }

})
