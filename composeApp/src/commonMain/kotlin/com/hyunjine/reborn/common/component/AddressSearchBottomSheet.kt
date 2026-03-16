package com.hyunjine.reborn.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography

/**
 * 카카오 주소 검색 바텀시트.
 * 카카오 우편번호 API를 ModalBottomSheet 내에서 표시하여 주소를 검색합니다.
 * @param onAddressSelected 주소 선택 완료 콜백 (도로명 주소 반환)
 * @param onDismiss 바텀시트 닫기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSearchBottomSheet(
    onAddressSelected: (address: String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Text(
            text = "주소 검색",
            style = typography.headingSemibold18,
            color = color.gray900,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        KakaoAddressSearchWebView(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(top = 12.dp),
            onAddressSelected = onAddressSelected
        )
    }
}

/**
 * AddressSearchBottomSheet 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun AddressSearchBottomSheetPreview() {
    RebornTheme {
        AddressSearchBottomSheet(
            onAddressSelected = {},
            onDismiss = {}
        )
    }
}
