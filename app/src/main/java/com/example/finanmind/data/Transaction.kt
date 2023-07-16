package com.example.finanmind.data

import java.math.BigDecimal
import java.util.Date
import java.util.UUID


data class Transaction(
    val uuid: String = UUID.randomUUID().toString(),
    val category: String = "",
    val value: BigDecimal = BigDecimal.valueOf(0),
    val date: Date = Date()
)
