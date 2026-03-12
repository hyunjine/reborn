package com.hyunjine.reborn.common.component

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Android 플랫폼 이미지 피커.
 * PhotoPicker API를 사용하여 갤러리에서 이미지를 선택합니다.
 * @param maxSelection 최대 선택 가능 이미지 수
 * @param onResult 선택된 이미지의 ByteArray 목록 콜백
 * @param content 이미지 피커를 실행할 트리거를 포함하는 Composable
 */
@Composable
actual fun ImagePickerLauncher(
    maxSelection: Int,
    onResult: (List<ByteArray>) -> Unit,
    content: @Composable (launch: () -> Unit) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxSelection)
    ) { uris: List<Uri> ->
        val byteArrays = uris.mapNotNull { uri ->
            uriToByteArray(context, uri)
        }
        onResult(byteArrays)
    }

    content {
        launcher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}

private fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
}
