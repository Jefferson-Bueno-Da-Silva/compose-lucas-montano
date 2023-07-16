package com.example.finanmind

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanmind.ui.theme.formatDate
import com.example.finanmind.ui.theme.randomTransaction
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun OverviewScreen(viewModel: OverviewViewModel = viewModel()) {
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = "Welcome back, \nJefferson",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ClearAll,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addTransaction(randomTransaction())
            }) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
                    AnimatedVisibility(visible = !showButton) {
                        Text(
                            text = "Add Transaction".toUpperCase(),
                            modifier = Modifier.padding(
                                start = 8.dp
                            )
                        )
                    }
                }
            }
        }
    ) {
        val uiState by viewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()
        val density = LocalDensity.current

        Column(modifier = Modifier.padding(it)) {
            LazyColumn(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                ),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 16.dp,
                ),
                state = listState
            ) {
                item {
                    AdviceToggle(uiState.advice)
                    Spacer(modifier = Modifier.padding(16.dp))
                }
                item {
                    Text(
                        text = "Transactions",
                        modifier = Modifier.padding(bottom = 16.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                items(uiState.transactions.size) { index ->
                    val transaction = uiState.transactions[index]
                    TransactionCard(
                        uui = transaction.uuid,
                        value = transaction.value,
                        category = transaction.category,
                        date = transaction.date.formatDate(),
                        onDelete = { deletedUuid ->
                            viewModel.deleteTransaction(deletedUuid)
                        }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = slideInVertically {
                with(density) { 100.dp.roundToPx() }
            },
            exit = slideOutVertically() {
                with(density) { 100.dp.roundToPx() }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.extraLarge
                        ),
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowCircleUp,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun AdviceToggle(advice: String) {
    var isVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isVisible = !isVisible }
        ) {
            Text(
                text = "Daily Insights",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall
            )
            Icon(
                imageVector = if (isVisible) {
                    Icons.Filled.ArrowDropUp
                } else {
                    Icons.Filled.ArrowDropDown
                },
                contentDescription = "Toggle"
            )
        }
        AnimatedVisibility(visible = isVisible) {
            Text(
                text = advice,
                modifier = Modifier.padding(
                    top = 16.dp
                ),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
