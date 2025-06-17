package com.example.weet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreDonut(
    score: Int,
    modifier: Modifier = Modifier,
    donutColor: Color = Color.Cyan,
    backgroundColor: Color = Color.LightGray
) {
    val sweepAngle = (score / 100f) * 360f

    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = modifier.size(100.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 배경 원
            drawArc(
                color = backgroundColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )

            // 점수 원
            drawArc(
                color = donutColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
        }

        // 중앙 점수 텍스트
        Text(
            text = "$score",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}