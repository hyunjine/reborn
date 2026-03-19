package com.hyunjine.reborn.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun<T> ImmutableList(
    size: Int, init: (index: Int) -> T
): ImmutableList<T> {
    return List(size = size, init = init).toImmutableList()
}