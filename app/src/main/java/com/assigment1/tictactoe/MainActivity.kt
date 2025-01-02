package com.assigment1.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assigment1.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        TicTacToeGame(modifier = Modifier.padding(innerPadding))
                    }
                )
            }
        }
    }
}

@Composable
fun TicTacToeGame(modifier: Modifier = Modifier) {
    var board by remember { mutableStateOf(Array(3) { Array(3) { ' ' } }) }
    var currentPlayer by remember { mutableStateOf('X') }
    var winner by remember { mutableStateOf<Char?>(null) }
    var isBoardFull by remember { mutableStateOf(false) }
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF1E1E2C), Color(0xFF232946))))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tic Tac Toe",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFEEBBC3),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when {
                        winner != null -> "Player $winner wins!"
                        isBoardFull -> "It's a draw!"
                        else -> "Player $currentPlayer's turn"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF4ACB7),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Board(
                    board = board,
                    onCellClick = { row, col ->
                        if (winner == null && board[row][col] == ' ') {
                            board[row][col] = currentPlayer
                            if (checkWinner(board, currentPlayer)) {
                                winner = currentPlayer
                                showModal = true
                            } else if (isBoardFull(board)) {
                                isBoardFull = true
                                showModal = true
                            } else {
                                currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                            }
                        }
                    }
                )
            }
        }

        if (showModal) {
            ModalDialog(
                onDismiss = {
                    board = Array(3) { Array(3) { ' ' } }
                    currentPlayer = 'X'
                    winner = null
                    isBoardFull = false
                    showModal = false
                },
                winner = winner
            )
        }
    }
}

@Composable
fun Board(board: Array<Array<Char>>, onCellClick: (Int, Int) -> Unit) {
    Column {
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    Cell(value = board[row][col], onClick = { onCellClick(row, col) })
                }
            }
        }
    }
}

@Composable
fun Cell(value: Char, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFBBE1FA), Color(0xFF3282B8))
                ),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = if (value == 'X') Color(0xFFE84545) else Color(0xFF66FCF1)
        )
    }
}

@Composable
fun ModalDialog(onDismiss: () -> Unit, winner: Char?) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4ACB7))
            ) {
                Text(
                    text = "Play Again",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        title = {
            Text(
                text = if (winner != null) "Player $winner Wins!" else "It's a Draw!",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFFEEBBC3)
            )
        },
        text = {
            Text(
                text = "Would you like to play another round?",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFFF4ACB7)
            )
        },
        containerColor = Color(0xFF232946),
        tonalElevation = 4.dp
    )
}

fun checkWinner(board: Array<Array<Char>>, player: Char): Boolean {
    for (i in 0..2) {
        if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
            (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
            return true
        }
    }
    return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
            (board[0][2] == player && board[1][1] == player && board[2][0] == player)
}

fun isBoardFull(board: Array<Array<Char>>): Boolean {
    for (row in board) {
        for (cell in row) {
            if (cell == ' ') {
                return false
            }
        }
    }
    return true
}
