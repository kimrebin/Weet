package com.example.weet.ui.screen.relationshipMap

import android.graphics.Paint
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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
//import kotlin.math.toRadians
import com.example.weet.ui.theme.Purple80
import java.lang.StrictMath.toRadians
//import com.example.weet.data.local.entity.toFriend
import com.example.weet.data.local.entity.Friend

@Composable
fun RelationshipMap(
    allFriends: List<Friend>,
    selectedTag: String
) {
    val friends = allFriends.filter { it.tag == selectedTag }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple80) // 💜 전체 배경 보라색
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)

            val innerRadius = 250f  // 점수 51~100
            val outerRadius = 420f  // 점수 0~50
            val nodeRadius = 50f

            // 1. 파란 원 영역만 흰색으로 채우기
            drawCircle(
                color = Color.White,
                radius = outerRadius,
                center = center
            )

            // 2. 친구 노드 배치
            friends.forEachIndexed { index, friend ->
                val angle = toRadians((360.0 / friends.size) * index)
                val relationshipScore = friend.score.coerceIn(0, 100)
                val distance = if (relationshipScore >= 51) innerRadius else outerRadius

                val friendX = center.x + cos(angle).toFloat() * distance
                val friendY = center.y + sin(angle).toFloat() * distance
                val friendOffset = Offset(friendX, friendY)

                drawLine(
                    color = Color.Black,
                    start = center,
                    end = friendOffset,
                    strokeWidth = 4f
                )

                drawCircle(
                    color = Color(0xFFA8E6A1), // 초록 원
                    radius = nodeRadius,
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

            // 3. 중앙 원 (빨간 테두리 없이 흰색 안에 아이콘만 표시)
            drawCircle(
                color = Color(0xFFCFCBDE),
                radius = 60f,
                center = center
            )
        }

        // 중앙 아이콘
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Me",
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center),
            tint = Color.Black
        )
    }
}