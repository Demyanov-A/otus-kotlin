@file:Suppress("unused")

package ru.otus.otuskotlin.m2l1

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SqlSelectBuilder {
    private var table: String? = null
    private var where: String? = null
    private var or: String? = null
    private val columns: MutableList<String> = mutableListOf()
    private val expressions: MutableList<String> = mutableListOf()

    fun from (str:String){
        this.table = str
    }

    fun select(vararg columns: String) {
        this.columns.addAll(columns)
    }

    fun where(block: () -> Unit){
        block()
        this.where = "$table where ${this.expressions.joinToString()}"
    }

    fun or(block: () -> Unit){
        block()
        this.or = "$table where (${this.expressions.joinToString(" or ")})"
    }

    infix fun String.eq(any: Any?) =
        expressions.add("$this ${when(any){
            is String -> "= '$any'"
            is Number -> "= $any"
            null -> "is $any"
            else -> "unknown"
        }}")

    infix fun String.nonEq(any: Any?) =
        expressions.add("$this ${when(any){
            is String -> "!= '$any'"
            is Number -> "!= $any"
            null -> "!is $any"
            else -> "unknown"
        }}")

    fun build(): String {
        requireNotNull(table) {"Table cannot be null"}
        val columns = this.columns.takeIf { it.isNotEmpty() }?.joinToString(", ")?: "*"
        val expression = or?: where?: table
        return "select $columns from $expression"
    }
}

fun query(block: SqlSelectBuilder.() -> Unit): SqlSelectBuilder {
    return SqlSelectBuilder().apply(block)
}

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class Hw1Sql {

    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }

    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `select certain columns from table 1`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"

        val real = query {
            from("table")
            where { "col_a" eq "id" }
        }

        checkSQL(expected, real)
    }

    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"

        val real = query {
            from("table")
            where {
                "col_a" nonEq 0
            }
        }

        checkSQL(expected, real)
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b !is null)"

        val real = query {
            from("table")
            where {
                or {"col_a" eq 4; "col_b" nonEq null}
            }
        }

        checkSQL(expected, real)
    }
}


