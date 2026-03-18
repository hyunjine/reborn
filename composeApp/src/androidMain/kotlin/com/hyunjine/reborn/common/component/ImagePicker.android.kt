package com.hyunjine.reborn.common.component

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Android ?Œë«???´ë?ì§€ ?¼ì»¤.
 * PhotoPicker APIë¥??¬ìš©?˜ì—¬ ê°¤ëŸ¬ë¦¬ì—???´ë?ì§€ë¥?? íƒ?©ë‹ˆ??
 * @param maxSelection ìµœë? ? íƒ ê°€???´ë?ì§€ ?? * @param onResult ? íƒ???´ë?ì§€??ByteArray ëª©ë¡ ì½œë°±
 * @param content ?´ë?ì§€ ?¼ì»¤ë¥??¤í–‰???¸ë¦¬ê±°ë? ?¬í•¨?˜ëŠ” Composable
 */
@Composable
actual fun ImagePickerLauncher(
    maxSelection: Int,
    onResult: (List<ByteArray>) -> Unit,
    content: @Composable (launch: () -> Unit) -> Unit
) {
    val context = LocalContext.current

    val singleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        val byteArrays = listOfNotNull(uri?.let { uriToByteArray(context, it) })
        onResult(byteArrays)
    }

    val multiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxSelection.coerceAtLeast(2))
    ) { uris: List<Uri> ->
        val byteArrays = uris.mapNotNull { uri ->
            uriToByteArray(context, uri)
        }
        onResult(byteArrays)
    }

    content {
        val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        if (maxSelection <= 1) {
            singleLauncher.launch(request)
        } else {
            multiLauncher.launch(request)
        }
    }
}

private fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
}
