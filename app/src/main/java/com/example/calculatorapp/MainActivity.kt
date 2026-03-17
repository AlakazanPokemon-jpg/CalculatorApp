package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                CalculadoraApp()
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CalculadoraPreview() {
//    MaterialTheme {
//        CalculadoraApp()
//    }
//}

@Composable
fun CalculadoraApp() {

    var display by remember { mutableStateOf("0") }
    var operacao by remember { mutableStateOf("") }
    var numeroAnterior by remember { mutableDoubleStateOf(0.0) }
    var novoNumero by remember { mutableStateOf(true) }

    fun onNumeroClick(numero: String) {
        display = if (novoNumero) {
            novoNumero = false
            numero
        } else {
            display + numero
        }
    }

    fun onOperacaoClick(op: String) {
        numeroAnterior = display.toDoubleOrNull() ?: 0.0
        operacao = op
        novoNumero = true
    }

    fun calcularResultado() {
        val numeroAtual = display.toDoubleOrNull() ?: 0.0

        val resultado = when (operacao) {
            "+" -> numeroAnterior + numeroAtual
            "-" -> numeroAnterior - numeroAtual
            "×" -> numeroAnterior * numeroAtual
            "÷" -> if (numeroAtual != 0.0) numeroAnterior / numeroAtual else 0.0
            else -> numeroAtual
        }

        display = if (resultado % 1.0 == 0.0) {
            resultado.toInt().toString()
        } else {
            resultado.toString()
        }

        novoNumero = true
    }

    fun limpar() {
        display = "0"
        operacao = ""
        numeroAnterior = 0.0
        novoNumero = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // DISPLAY
        Text(
            text = display,
            fontSize = 48.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.End
        )

        // BOTÕES
        val botoes = listOf(
            listOf("7", "8", "9", "÷"),
            listOf("4", "5", "6", "×"),
            listOf("1", "2", "3", "-"),
            listOf("C", "0", "=", "+")
        )

        Column {
            botoes.forEach { linha ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    linha.forEach { texto ->
                        Button(
                            onClick = {
                                when (texto) {
                                    in "0".."9" -> onNumeroClick(texto)
                                    "+", "-", "×", "÷" -> onOperacaoClick(texto)
                                    "=" -> calcularResultado()
                                    "C" -> limpar()
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                                .height(80.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when (texto) {
                                    in "0".."9" -> Color.DarkGray
                                    "C" -> Color.Red
                                    "=" -> Color(0xFF4CAF50)
                                    else -> Color(0xFFFF9800)
                                }
                            )
                        ) {
                            Text(
                                text = texto,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}