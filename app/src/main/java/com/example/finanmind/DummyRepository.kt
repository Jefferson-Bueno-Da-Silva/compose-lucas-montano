package com.example.finanmind

import com.example.finanmind.data.Transaction

object DummyRepository {
    private var _transactions = mutableListOf<Transaction>()
    val transactions get() = _transactions.toList()

    fun add(transaction: Transaction) {
        _transactions.add(transaction)
    }

    fun delete(uuid: String) {
        _transactions.removeIf {
            uuid === it.uuid
        }
    }

    fun update(transaction: Transaction) {
        delete(transaction.uuid)
        _transactions.add(transaction)
    }

    fun clear() {
        _transactions.clear()
    }

    fun find(uuid: String): Transaction {
        return _transactions.firstOrNull { it.uuid === uuid } ?: Transaction()
    }
}
