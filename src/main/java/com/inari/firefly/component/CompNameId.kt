package com.inari.firefly.component

import com.inari.firefly.Named
import com.inari.util.indexed.IIndexedTypeKey

interface CompNameId: Named {
    val typeKey: IIndexedTypeKey
}
