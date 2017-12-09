package com.inari.firefly.component

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Singleton
