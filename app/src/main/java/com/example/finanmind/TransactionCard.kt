package com.example.finanmind

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ContentAlpha
import com.example.finanmind.ui.theme.categories
import com.example.finanmind.ui.theme.toCurrency
import java.math.BigDecimal

@Composable
fun TransactionCard(
    uui: String,
    value: BigDecimal,
    category: String,
    date: String,
    onDelete: (uuid: String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        TransactionInfoRow(category, date, value, onClickDelete = { onDelete(uui) })
    }
}

@Composable
private fun TransactionInfoRow(
    category: String,
    date: String,
    value: BigDecimal,
    onClickDelete: () -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = categories.firstOrNull { it.first === category }?.second
                ?: Icons.Filled.Lightbulb,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = category, style = MaterialTheme.typography.titleMedium)
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium)
            )

        }

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value.toCurrency(),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = onClickDelete) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
        }
    }
}

@Preview
@Composable
fun Preview() {
    TransactionCard(
        uui = "",
        date = "mai 07, 06:16 PM",
        value = BigDecimal.valueOf(18.23),
        category = "restaurant",
        onDelete = {}
    )
}
