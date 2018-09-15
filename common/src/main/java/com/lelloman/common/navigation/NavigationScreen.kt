package com.lelloman.common.navigation

import com.lelloman.common.utils.Named
import kotlin.reflect.KClass

interface NavigationScreen : Named {
    val clazz: KClass<*>
    val deepLinkStartable: DeepLinkStartable?
}
