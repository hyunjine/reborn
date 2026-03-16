package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import kotlinx.collections.immutable.persistentListOf

/**
 * 품목 선택 바텀시트.
 * 매입 품목 항목을 선택할 수 있는 ModalBottomSheet입니다.
 * @param onItemSelected 항목 선택 시 콜백
 * @param onDismiss 바텀시트 닫기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ItemPickerBottomSheet(
    onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val items = persistentListOf(
        "고철", "스텐", "구리", "알루미늄", "기판", "납", "비철", "철", "황동", "전선", "모터", "기타"
    )

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
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "품목 선택",
                style = typography.headingSemibold18,
                color = color.gray900
            )
            Spacer(modifier = Modifier.height(24.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 3
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .background(color.gray50, RoundedCornerShape(10.dp))
                            .border(1.dp, color.gray200, RoundedCornerShape(10.dp))
                            .clickable {
                                onItemSelected(item)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            style = typography.bodyMedium14,
                            color = color.gray800
                        )
                    }
                }
            }
        }
    }
}

/**
 * ItemPickerBottomSheet 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun ItemPickerBottomSheetPreview() {
    RebornTheme {
        ItemPickerBottomSheet(
            onItemSelected = {},
            onDismiss = {}
        )
    }
}
