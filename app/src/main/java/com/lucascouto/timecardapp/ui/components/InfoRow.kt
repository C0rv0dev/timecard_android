package com.lucascouto.timecardapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils

@Composable
fun InfoRow(
    label: String,
    value: Float?,
    isDayFormat: Boolean = false,
    isHourFormat: Boolean = false,
    isMoneyFormat: Boolean = false,
    isEarning: Boolean = false,
    isDeduction: Boolean = false,
    highlight: Boolean = false
) {
    val gapSize = 10
    val textColor = if (isDeduction)
        Color(0xFF8B0000)
    else if (isEarning)
        Color(0xFF006400)
    else
        Color.Unspecified

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            label,
            style = TextStyle(
                fontSize = 16.sp,
                color = textColor
            )
        )

        val dashColor = DividerDefaults.color
        Canvas(
            modifier = Modifier
                .height(1.dp)
                .padding(horizontal = gapSize.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            val dashWidth = 10f
            val dashGap = 10f
            val y = size.height / 2
            var startX = 0f
            while (startX < size.width) {
                val endX = (startX + dashWidth).coerceAtMost(size.width)
                drawLine(
                    color = dashColor,
                    start = androidx.compose.ui.geometry.Offset(startX, y),
                    end = androidx.compose.ui.geometry.Offset(endX, y),
                    strokeWidth = DividerDefaults.Thickness.toPx()
                )
                startX += dashWidth + dashGap
            }
        }

        Text(
            text = getTextualRepresentation(
                value,
                isDayFormat = isDayFormat,
                isHourFormat = isHourFormat,
                isMoneyFormat = isMoneyFormat
            ),
            style = TextStyle(
                fontSize = if (highlight) 20.sp else 16.sp,
                color = textColor
            )
        )
    }
}

private fun getTextualRepresentation(
    value: Float?,
    isDayFormat: Boolean = false,
    isHourFormat: Boolean = false,
    isMoneyFormat: Boolean = false
): String {
    return when {
        value == null -> "--"
        isDayFormat -> "${value.toInt()} day${if (value.toInt() != 1) "s" else ""}"
        isMoneyFormat -> "¥${"%,d".format(value.toInt())}"
        isHourFormat -> TimeUtils.formatHoursToHHMM(value)

        else -> value.toString()
    }
}