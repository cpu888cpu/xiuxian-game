package com.karios.xiuxian.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karios.xiuxian.ui.theme.BloodRed
import com.karios.xiuxian.ui.theme.Gold
import com.karios.xiuxian.ui.theme.GoldLight
import com.karios.xiuxian.ui.theme.InkBlack
import com.karios.xiuxian.ui.theme.InkLine
import com.karios.xiuxian.ui.theme.InkSurface
import com.karios.xiuxian.ui.theme.InkSurface2
import com.karios.xiuxian.ui.theme.JadeGreen
import com.karios.xiuxian.ui.theme.MutedText
import com.karios.xiuxian.ui.theme.PaperText
import com.karios.xiuxian.ui.theme.SerifFont

/** 自定义进度条，避免不同 material3 版本 API 差异 */
@Composable
fun InkProgress(
    value: Float,
    color: Color,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp
) {
    Box(
        modifier
            .height(height)
            .fillMaxWidth()
            .clip(RoundedCornerShape(height / 2))
            .background(InkSurface2)
            .border(1.dp, InkLine, RoundedCornerShape(height / 2))
    ) {
        val v = value.coerceIn(0f, 1f)
        if (v > 0f) {
            Box(
                Modifier
                    .fillMaxWidth(v)
                    .fillMaxHeight()
                    .background(color)
            )
        }
    }
}

@Composable
fun GoldButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Gold,
            contentColor = InkBlack,
            disabledContainerColor = InkSurface2,
            disabledContentColor = MutedText
        )
    ) {
        Text(
            text = text,
            fontFamily = SerifFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun GhostButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = GoldLight,
            disabledContentColor = MutedText
        )
    ) {
        Text(
            text = text,
            fontFamily = SerifFont,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PanelCard(
    modifier: Modifier = Modifier,
    borderColor: Color = InkLine,
    content: @Composable () -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(InkSurface)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column { content() }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .width(3.dp)
                .height(14.dp)
                .background(Gold)
        )
        Text(
            text = text,
            fontFamily = SerifFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun KeyValueRow(key: String, value: String, valueColor: Color = PaperText) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(key, fontFamily = SerifFont, fontSize = 13.sp, color = MutedText)
        Text(value, fontFamily = SerifFont, fontSize = 13.sp, color = valueColor)
    }
}

@Composable
fun ColorChip(text: String, color: Color) {
    Box(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, fontFamily = SerifFont, fontSize = 11.sp, color = color)
    }
}

@Composable
fun ListRow(
    title: String,
    subtitle: String = "",
    trailing: String = "",
    trailingColor: Color = JadeGreen,
    titleColor: Color = PaperText,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .background(InkSurface)
            .border(1.dp, InkLine, RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                title,
                fontFamily = SerifFont,
                fontSize = 15.sp,
                color = if (enabled) titleColor else MutedText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    subtitle,
                    fontFamily = SerifFont,
                    fontSize = 12.sp,
                    color = MutedText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (trailing.isNotEmpty()) {
            Text(
                trailing,
                fontFamily = SerifFont,
                fontSize = 13.sp,
                color = if (enabled) trailingColor else MutedText
            )
        }
    }
}

val BadColor = BloodRed