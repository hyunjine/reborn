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
 * ?ˆëª© ? íƒ ë°”í??œíŠ¸.
 * ë§¤ìž… ?ˆëª© ??ª©??? íƒ?????ˆëŠ” ModalBottomSheet?…ë‹ˆ??
 * @param onItemSelected ??ª© ? íƒ ??ì½œë°±
 * @param onDismiss ë°”í??œíŠ¸ ?«ê¸° ì½œë°±
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ItemPickerBottomSheet(
    onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val items = persistentListOf(
        "ê³ ì² ", "?¤í…", "êµ¬ë¦¬", "?Œë£¨ë¯¸ëŠ„", "ê¸°íŒ", "??, "ë¹„ì² ", "ì²?, "?©ë™", "?„ì„ ", "ëª¨í„°", "ê¸°í?", "ì§ì ‘ ?…ë ¥"
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
                text = "?ˆëª© ? íƒ",
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
 * ItemPickerBottomSheet ë¯¸ë¦¬ë³´ê¸°.
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
