package com.example.finanmind

import androidx.core.graphics.translationMatrix
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanmind.data.Transaction
import com.example.finanmind.network.AiService
import com.example.finanmind.network.OpenAIPrompt
import com.example.finanmind.network.RetrofitModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class OverviewViewModel(
    private val repository: DummyRepository = DummyRepository,
    private val aiService: AiService = RetrofitModule.provideAiService(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        val prompt = "Give me a small personal finance advice"
        viewModelScope.launch(ioDispatcher) {
            val response = aiService.completions(OpenAIPrompt(prompt))
            if (response.isSuccessful) {
                response.body()?.choices?.first()?.text?.let { advice ->
                    _uiState.value = uiState.value.copy(
                        advice = advice.trim()
                    )
                }
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        repository.add(transaction)
        updateState()
    }

    fun deleteTransaction(uuid: String) {
        repository.delete(uuid)
        updateState()
    }

    private fun updateState() {
        val transactions = repository.transactions
        _uiState.value = uiState.value.copy(
            transactions = transactions,
            total = transactions.sumOf { it.value }
        )
    }

    data class UiState(
        val advice: String = "Essa seria a dica vindo da OpenIA",
        val userName: String = "Jefferson \nBueno",
        val transactions: List<Transaction> = emptyList(),
        val total: BigDecimal = transactions.sumOf { it.value }
    )
}




