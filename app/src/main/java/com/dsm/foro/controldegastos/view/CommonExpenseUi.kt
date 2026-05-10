package com.dsm.foro.controldegastos.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta compartida de la app.
internal val BgDark = Color(0xFF0F1117)
internal val BgCard = Color(0xFF1A1D27)
internal val BgField = Color(0xFF22263A)
internal val AccentGreen = Color(0xFF00E5A0)
internal val AccentGreenDim = Color(0x1A00E5A0)
internal val TextPrimary = Color(0xFFF0F2FF)
internal val TextSecondary = Color(0xFF7A7F9A)
internal val TextHint = Color(0xFF3D4261)
internal val ErrorRed = Color(0xFFFF5C7A)
internal val DividerColor = Color(0xFF2A2E45)

internal val expenseCategories = listOf(
    "Alimentación",
    "Transporte",
    "Salud",
    "Educación",
    "Entretenimiento",
    "Otros"
)

internal val categoryIcons: Map<String, ImageVector> = mapOf(
    "Alimentación" to Icons.Rounded.Restaurant,
    "Transporte" to Icons.Rounded.DirectionsCar,
    "Salud" to Icons.Rounded.LocalHospital,
    "Educación" to Icons.Rounded.School,
    "Entretenimiento" to Icons.Rounded.SportsEsports,
    "Otros" to Icons.Rounded.Category
)

//label personalizado
@Composable
internal fun ExpenseFieldLabel(text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentGreen,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text.uppercase(),
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

//Campo de texto personalizado
@Composable
internal fun ExpenseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    textSize: Int = 13
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BgField)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prefix != null) {
            Text(
                text = prefix,
                color = AccentGreen,
                fontWeight = FontWeight.Bold,
                fontSize = textSize.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = TextPrimary,
                fontSize = textSize.sp,
                fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            singleLine = true,
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextHint,
                        fontSize = textSize.sp
                    )
                }
                inner()
            }
        )

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(6.dp))
            trailingIcon()
        }
    }
}

//chip de categorias
@Composable
internal fun CategoryChip(
    label: String,
    icon: ImageVector = categoryIcons[label] ?: Icons.Rounded.Category,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) AccentGreenDim else BgField)
            .border(
                width = 1.5.dp,
                color = if (selected) AccentGreen else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) AccentGreen else TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = if (selected) AccentGreen else TextSecondary,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
