package com.github.andrewgazelka.kotlin_ai

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

fun shouldNotMatch() = object : Matcher<String> {
    override fun test(value: String) =
        MatcherResult(value.contains("foo"), "String $value should include foo", "String $value should not include foo")
}

class StringSpecExample : StringSpec({
    "your test case" {
        val whiteSpace = """\s""".toRegex()
        val a = "hello: goodbye"
        val b = "    hello: goodbye"
    }
})
