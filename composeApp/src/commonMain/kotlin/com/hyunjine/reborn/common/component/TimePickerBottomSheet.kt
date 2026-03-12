package com.hyunjine.reborn.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography

/**
 * 시간 선택 바텀시트.
 * 시/분을 WheelPicker로 선택할 수 있는 ModalBottomSheet입니다.
 * @param initialHour 초기 시간 (0~23)
 * @param initialMinute 초기 분 (0~59)
 * @param onConfirm 확인 버튼 클릭 시 (hour, minute) 콜백
 * @param onDismiss 바텀시트 닫기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBottomSheet(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedHour by rememberSaveable { mutableIntStateOf(initialHour) }
    var selectedMinute by rememberSaveable { mutableIntStateOf(initialMinute) }

    val hours = (0..23).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "시간 선택",
                style = typography.headingSemibold18,
                color = color.gray900
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WheelPicker(
                    items = hours,
                    selectedIndex = selectedHour,
                    onSelectedChanged = { selectedHour = it },
                    modifier = Modifier.width(80.dp)
                )
                Text(
                    text = ":",
                    style = typography.headingBold24,
                    color = color.gray900,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                WheelPicker(
                    items = minutes,
                    selectedIndex = selectedMinute,
                    onSelectedChanged = { selectedMinute = it },
                    modifier = Modifier.width(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onConfirm(selectedHour, selectedMinute) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color.green500)
            ) {
                Text(
                    text = "확인",
                    style = typography.bodyMedium14,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * TimePickerBottomSheet 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun TimePickerBottomSheetPreview() {
    RebornTheme {
        TimePickerBottomSheet(
            initialHour = 9,
            initialMinute = 0,
            onConfirm = { _, _ -> },
            onDismiss = {}
        )
    }
}
