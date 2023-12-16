package com.musinsa.server

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TestTemplate: BehaviorSpec({

    val fixture = kotlinFixture()

    Given("테스트") {
        val a = 1
        val b = 1

        When("더하기") {
            val result = a + b
            Then("2") {
                result shouldBe 2
            }
        }

        When("빼기") {
            val result = a - b
            Then("0") {
                result shouldBe 0
            }
        }

        When("Person") {
            val person = fixture<Person> {
                property(Person::name) { "iskwon" }
            }

            Then("name") {
                person.name shouldBe "iskwon"
            }
        }
    }
})

class Person(val name: String, val age: Int)
