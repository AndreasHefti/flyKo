package com.inari.firefly.component

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.Named

class CompNameId(
    @JvmField val typeKey: IIndexedTypeKey,
    override val name: String
) : Named