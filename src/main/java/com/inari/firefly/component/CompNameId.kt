package com.inari.firefly.component

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.Named

interface CompNameId: Named {
    val typeKey: IIndexedTypeKey
}
