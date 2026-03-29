package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.common.util.animClickable

/**
 * Reborn 앱의 공통 모달 다이얼로그 컴포넌트입니다.
 *
 * Figma 디자인 시스템의 modal 컴포넌트를 구현합니다.
 * [RebornModalState]를 통해 positive(초록색 확인 버튼) 또는 negative(빨간색 확인 버튼) 스타일을 지정할 수 있습니다.
 *
 * @param title 모달 제목입니다. 최대 1줄까지 표시됩니다.
 * @param onDismiss 모달이 닫힐 때 호출되는 콜백입니다.
 * @param onConfirm 확인 버튼 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 * @param content 모달 내용입니다. null이면 내용 영역이 표시되지 않습니다. 최대 3줄까지 표시됩니다.
 * @param state 모달 상태입니다. [RebornModalState.Positive] 또는 [RebornModalState.Negative]를 지정합니다.
 * @param cancelText 취소 버튼 텍스트입니다. null이면 확인 버튼만 표시됩니다.
 * @param confirmText 확인 버튼 텍스트입니다.
 */
@Composable
fun Modal(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    content: String? = null,
    state: RebornModalState = RebornModalState.Positive,
    cancelText: String? = "취소",
    confirmText: String = "확인",
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        RebornModalContent(
            title = title,
            content = content,
            state = state,
            cancelText = cancelText,
            confirmText = confirmText,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            modifier = modifier,
        )
    }
}

/**
 * 모달 다이얼로그의 내부 콘텐츠 컴포넌트입니다.
 *
 * @param title 모달 제목입니다.
 * @param content 모달 내용입니다. null이면 표시되지 않습니다.
 * @param state 모달 상태입니다.
 * @param cancelText 취소 버튼 텍스트입니다. null이면 확인 버튼만 표시됩니다.
 * @param confirmText 확인 버튼 텍스트입니다.
 * @param onDismiss 취소 버튼 클릭 시 호출되는 콜백입니다.
 * @param onConfirm 확인 버튼 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 */
@Composable
fun RebornModalContent(
    title: String,
    content: String?,
    state: RebornModalState,
    cancelText: String?,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(32.dp)
    Column(
        modifier = modifier
            .width(318.dp)
            .clip(shape)
            .background(color.white)
            .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // 제목 + 내용 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = typography.headingMedium20.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF141414),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (content != null) {
                Text(
                    text = content,
                    style = typography.bodyRegular16,
                    color = Color(0xFF474747),
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        // 버튼 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 취소 버튼
            if (cancelText != null) {
                val cancelShape = RoundedCornerShape(14.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(cancelShape)
                        .background(color.gray50)
                        .animClickable(shape = cancelShape, onClick = onDismiss)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = cancelText,
                        style = typography.titleBold16,
                        color = color.gray500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // 확인 버튼
            val confirmShape = RoundedCornerShape(14.dp)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(confirmShape)
                    .background(state.confirmButtonColor)
                    .animClickable(shape = confirmShape, onClick = onConfirm)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = confirmText,
                    style = typography.titleBold16,
                    color = state.confirmTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * 모달 상태를 정의합니다.
 *
 * @property confirmButtonColor 확인 버튼 배경색입니다.
 * @property confirmTextColor 확인 버튼 텍스트 색상입니다.
 */
enum class RebornModalState(
    val confirmButtonColor: Color,
    val confirmTextColor: Color,
) {
    /** 긍정 액션 상태입니다. 초록색 확인 버튼으로 표시됩니다. */
    Positive(
        confirmButtonColor = Color(0xFF22C55E),
        confirmTextColor = Color.White,
    ),

    /** 부정 액션 상태입니다. 빨간색 확인 버튼으로 표시됩니다. */
    Negative(
        confirmButtonColor = Color(0xFFFBD5D0),
        confirmTextColor = Color(0xFFFB2C36),
    ),
}

/**
 * RebornModal Positive 상태 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun RebornModalPositivePreview() {
    RebornTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            RebornModalContent(
                title = "예약을 확정하시겠습니까?",
                content = "확정 후에는 취소가 어려울 수 있습니다.",
                state = RebornModalState.Positive,
                cancelText = "취소",
                confirmText = "확인",
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}

/**
 * RebornModal Negative 상태 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun RebornModalNegativePreview() {
    RebornTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            RebornModalContent(
                title = "예약을 취소하시겠습니까?",
                content = "취소된 예약은 복구할 수 없습니다.",
                state = RebornModalState.Negative,
                cancelText = "취소",
                confirmText = "확인",
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}

/**
 * RebornModal 제목만 있는 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun RebornModalTitleOnlyPreview() {
    RebornTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            RebornModalContent(
                title = "로그아웃 하시겠습니까?",
                content = null,
                state = RebornModalState.Positive,
                cancelText = "취소",
                confirmText = "확인",
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}

/**
 * RebornModal 버튼 1개 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun RebornModalSingleButtonPreview() {
    RebornTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            RebornModalContent(
                title = "업체 등록이 완료되었습니다",
                content = "업체 정보를 확인해주세요.",
                state = RebornModalState.Positive,
                cancelText = null,
                confirmText = "확인",
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
