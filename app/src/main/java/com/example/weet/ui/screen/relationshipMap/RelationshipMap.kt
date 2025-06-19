package com.example.weet.ui.screen.relationshipMap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.weet.data.local.entity.Friend
import android.graphics.Paint
import kotlin.math.*
import androidx.compose.foundation.layout.BoxScope
import com.example.weet.ui.theme.Purple80

//import androidx.compose.foundation.layout.align


@Composable
fun RelationshipMap(friends: List<Friend>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1FFFF)) // 💜 연보라 배경
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val spacing = 250f
            val radius = 50f
            val textOffsetY = 20f

            // ✅ 중앙 원 먼저 그림 (배경 역할)
            drawCircle(
                color = Color.LightGray.copy(alpha = 1.0f), // 완전 불투명
                radius = 60f,
                center = center
            )

            // ✅ 그 다음 친구 노드들과 선을 그림 (위에 올라오게)
            friends.forEachIndexed { index, friend ->
                val angle = Math.toRadians((360.0 / friends.size) * index)
                val friendX = center.x + cos(angle).toFloat() * spacing
                val friendY = center.y + sin(angle).toFloat() * spacing
                val friendOffset = Offset(friendX, friendY)

                drawLine(
                    color = Color.Black,
                    start = center,
                    end = friendOffset,
                    strokeWidth = 4f
                )

                drawCircle(
                    color = Color(0xFFA8E6A1),
                    radius = radius,
                    center = friendOffset
                )

                drawContext.canvas.nativeCanvas.drawText(
                    friend.name,
                    friendX - 30f,
                    friendY + 10f,
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 28f
                    }
                )
            }
        }


        // 👉 Icon을 Box 내부에 포함
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Me",
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center), // 이제 정상 작동
            tint = Color.Black
        )
    }
}
