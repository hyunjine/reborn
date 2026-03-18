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
 * iOS 플랫폼 이미지 피커.
 * PHPickerViewController를 사용하여 갤러리에서 이미지를 선택합니다.
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
