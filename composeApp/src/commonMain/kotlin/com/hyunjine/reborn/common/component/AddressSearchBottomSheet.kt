package com.hyunjine.reborn.common.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_arrow_left

/**
 * 카카??주소 검???�체?�면 ?�이?�로�?
 * 카카???�편번호 API�??�체?�면?�로 ?�시?�여 주소�?검?�합?�다.
 * @param onAddressSelected 주소 ?�택 ?�료 콜백 (?�로�?주소 반환)
 * @param onDismiss ?�기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSearchDialog(
    onAddressSelected: (address: String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "주소 검??,
                            style = typography.headingMedium18,
                            color = color.gray900
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                painter = painterResource(Res.drawable.icon_24_arrow_left),
                                contentDescription = "?�기",
                                modifier = Modifier.size(24.dp),
                                tint = color.gray900
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            containerColor = Color.White
        ) { padding ->
            KakaoAddressSearchWebView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                onAddressSelected = onAddressSelected
            )
        }
    }
}

/**
 * AddressSearchDialog 미리보기.
 */
@Preview(showBackground = true)
@Composable
private fun AddressSearchDialogPreview() {
    RebornTheme {
        AddressSearchDialog(
            onAddressSelected = {},
            onDismiss = {}
        )
    }
}
