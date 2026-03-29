package com.hyunjine.reborn.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.common.util.animClickable
import com.hyunjine.reborn.data.store.model.Distance
import com.hyunjine.reborn.data.store.model.MatterModel
import com.hyunjine.reborn.data.store.model.StoreModel
import com.hyunjine.reborn.util.readable
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_location

/**
 * 고물상 목록에서 개별 고물상 정보를 표시하는 아이템입니다.
 * @param store 고물상 데이터 모델입니다.
 * @param onClick 아이템 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 */
@Composable
fun StoreListItem(
    store: StoreModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .animClickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = store.imageUrl,
            contentDescription = "${store.name} 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(color.gray100)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = store.name,
                    style = typography.headingSemibold18,
                    color = color.gray900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_24_location),
                        contentDescription = null,
                        tint = color.gray600,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = store.distance.toString(),
                        style = typography.bodyRegular14,
                        color = color.gray600,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                store.prices.take(2).forEach { price ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = price.name,
                            style = typography.bodyRegular14,
                            color = color.gray700,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = price.price.readable() + "원/" + price.unit,
                            style = typography.bodySemibold14,
                            color = color.gray900
                        )
                    }
                }
            }
        }
    }
}

/**
 * StoreListItem 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun StoreListItemPreview() {
    RebornTheme {
        StoreListItem(
            store = StoreModel(
                id = 1L,
                name = "서울철강",
                imageUrl = "",
                distance = Distance.meters(3100),
                prices = persistentListOf(
                    MatterModel("철근", price = 7200, unit = "kg"),
                    MatterModel("동", price = 7200, unit = "kg"),
                )
            ),
            onClick = {}
        )
    }
}
