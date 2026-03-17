package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.ui.theme.CalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF121212) // Fundo moderno escuro (Dark Mode)
                ) {
                    CalculadoraScreen()
                }
            }
        }
    }
}

@Composable
fun CalculadoraScreen() {
    // --- ESTADOS ---
    var displayValue by remember { mutableStateOf("0") }
    var primeiroValor by remember { mutableStateOf<Double?>(null) }
    var operador by remember { mutableStateOf<String?>(null) }
    var aguardandoProximoNumero by remember { mutableStateOf(false) }

    // --- LÓGICA DO VISOR COMPLETO ---
    // Esta variável avalia os estados e monta a frase "1 + 3" para a tela
    val textoVisor = if (primeiroValor != null && operador != null) {
        // Tira o .0 do primeiro valor se for inteiro para ficar bonito na tela
        val primeiroFormatado = if (primeiroValor!! % 1.0 == 0.0) {
            primeiroValor!!.toLong().toString()
        } else {
            primeiroValor!!.toString()
        }

        if (aguardandoProximoNumero) {
            "$primeiroFormatado $operador " // Mostra ex: "1 + "
        } else {
            "$primeiroFormatado $operador $displayValue" // Mostra ex: "1 + 3"
        }
    } else {
        displayValue // Mostra só o número atual se não tiver operador
    }

    // --- FUNÇÕES DE AÇÃO ---
    fun onNumeroClick(numero: String) {
        if (displayValue == "0" || aguardandoProximoNumero) {
            displayValue = numero
            aguardandoProximoNumero = false
        } else {
            if (displayValue.length < 10) {
                displayValue += numero
            }
        }
    }

    fun onOperadorClick(op: String) {
        primeiroValor = displayValue.toDoubleOrNull()
        operador = op
        aguardandoProximoNumero = true
    }

    fun onLimparClick() {
        displayValue = "0"
        primeiroValor = null
        operador = null
        aguardandoProximoNumero = false
    }

    fun onIgualClick() {
        val segundoValor = displayValue.toDoubleOrNull()

        if (primeiroValor != null && segundoValor != null && operador != null) {
            val resultado = when (operador) {
                "+" -> primeiroValor!! + segundoValor
                "-" -> primeiroValor!! - segundoValor
                "*" -> primeiroValor!! * segundoValor
                "/" -> if (segundoValor != 0.0) primeiroValor!! / segundoValor else Double.NaN
                else -> 0.0
            }

            val resultadoFormatado = if (resultado % 1.0 == 0.0) {
                resultado.toLong().toString()
            } else {
                resultado.toString()
            }

            displayValue = if (resultadoFormatado.length > 15) {
                "Erro"
            } else {
                resultadoFormatado
            }

            primeiroValor = null
            operador = null
            aguardandoProximoNumero = true
        }
    }

    // --- CORES DA UI ---
    val corNumero = Color(0xFF333333)
    val corOperador = Color(0xFFFF9500)
    val corLimpar = Color(0xFFD32F2F)
    val corTextoBranco = Color.White

    // --- INTERFACE VISUAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Visor (Agora usa a variável textoVisor)
        Text(
            text = textoVisor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            textAlign = TextAlign.End,
            color = corTextoBranco,
            fontSize = 64.sp, // Fonte levemente menor para caber toda a expressão
            fontWeight = FontWeight.Light,
            maxLines = 2 // Permite que a conta quebre linha se for muito grande
        )

        // Linha 1
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CalculadoraBotao("7", Modifier.weight(1f), corNumero) { onNumeroClick("7") }
            CalculadoraBotao("8", Modifier.weight(1f), corNumero) { onNumeroClick("8") }
            CalculadoraBotao("9", Modifier.weight(1f), corNumero) { onNumeroClick("9") }
            CalculadoraBotao("/", Modifier.weight(1f), corOperador) { onOperadorClick("/") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Linha 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CalculadoraBotao("4", Modifier.weight(1f), corNumero) { onNumeroClick("4") }
            CalculadoraBotao("5", Modifier.weight(1f), corNumero) { onNumeroClick("5") }
            CalculadoraBotao("6", Modifier.weight(1f), corNumero) { onNumeroClick("6") }
            CalculadoraBotao("*", Modifier.weight(1f), corOperador) { onOperadorClick("*") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Linha 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CalculadoraBotao("1", Modifier.weight(1f), corNumero) { onNumeroClick("1") }
            CalculadoraBotao("2", Modifier.weight(1f), corNumero) { onNumeroClick("2") }
            CalculadoraBotao("3", Modifier.weight(1f), corNumero) { onNumeroClick("3") }
            CalculadoraBotao("-", Modifier.weight(1f), corOperador) { onOperadorClick("-") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Linha 4
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CalculadoraBotao("0", Modifier.weight(1f), corNumero) { onNumeroClick("0") }
            CalculadoraBotao("C", Modifier.weight(1f), corLimpar) { onLimparClick() }
            CalculadoraBotao("=", Modifier.weight(1f), corOperador) { onIgualClick() }
            CalculadoraBotao("+", Modifier.weight(1f), corOperador) { onOperadorClick("+") }
        }
    }
}

@Composable
fun CalculadoraBotao(
    simbolo: String,
    modifier: Modifier = Modifier,
    corFundo: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(84.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = corFundo
        )
    ) {
        Text(
            text = simbolo,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    CalculatorAppTheme {
        Surface(color = Color(0xFF121212)) {
            CalculadoraScreen()
        }
    }
}