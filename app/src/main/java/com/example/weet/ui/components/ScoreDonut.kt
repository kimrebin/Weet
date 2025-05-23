package com.example.weet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun ScoreDonut(score: Int) {
    Canvas(modifier = Modifier.size(100.dp)) {
        val sweep = (score / 100f) * 360f
        drawArc(
            color = Color.LightGray,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(16f)
        )
        drawArc(
            color = MaterialTheme.colorScheme.primary,
            startAngle = -90f,
            sweepAngle = sweep,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(16f, cap = StrokeCap.Round)
        )
    }
}

