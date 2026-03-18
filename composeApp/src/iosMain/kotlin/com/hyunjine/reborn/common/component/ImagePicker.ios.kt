package com.hyunjine.reborn.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationSelectionOrdered
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.posix.memcpy

/**
 * iOS ?Œë«???´ë?ì§€ ?¼ì»¤.
 * PHPickerViewControllerë¥??¬ìš©?˜ì—¬ ê°¤ëŸ¬ë¦¬ì—???´ë?ì§€ë¥?? íƒ?©ë‹ˆ??
 * @param maxSelection ìµœë? ? íƒ ê°€???´ë?ì§€ ?? * @param onResult ? íƒ???´ë?ì§€??ByteArray ëª©ë¡ ì½œë°±
 * @param content ?´ë?ì§€ ?¼ì»¤ë¥??¤í–‰???¸ë¦¬ê±°ë? ?¬í•¨?˜ëŠ” Composable
 */
@Composable
actual fun ImagePickerLauncher(
    maxSelection: Int,
    onResult: (List<ByteArray>) -> Unit,
    content: @Composable (launch: () -> Unit) -> Unit
) {
    val delegate = remember {
        object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                picker.dismissViewControllerAnimated(true, null)
                val results = didFinishPicking.filterIsInstance<PHPickerResult>()
                if (results.isEmpty()) {
                    onResult(emptyList())
                    return
                }
                val byteArrays = mutableListOf<ByteArray>()
                var remaining = results.size
                results.forEach { result ->
                    result.itemProvider.loadObject(
                        forTypeIdentifier = "public.image"
                    ) { reading, _ ->
                        val image = reading as? UIImage
                        if (image != null) {
                            val data = UIImageJPEGRepresentation(image, 0.8)
                            if (data != null) {
                                val bytes = data.toByteArray()
                                synchronized(byteArrays) { byteArrays.add(bytes) }
                            }
                        }
                        remaining--
                        if (remaining == 0) {
                            onResult(byteArrays.toList())
                        }
                    }
                }
            }
        }
    }

    content {
        val configuration = PHPickerConfiguration().apply {
            selectionLimit = maxSelection.toLong()
            filter = PHPickerFilter.imagesFilter
            selection = PHPickerConfigurationSelectionOrdered
        }
        val picker = PHPickerViewController(configuration = configuration)
        picker.delegate = delegate
        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(picker, animated = true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    val byteArray = ByteArray(size)
    if (size > 0) {
        memcpy(byteArray.refTo(0), bytes, length)
    }
    return byteArray
}
