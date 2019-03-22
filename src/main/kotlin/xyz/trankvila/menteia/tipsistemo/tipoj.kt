package xyz.trankvila.menteia.tipsistemo

import kotlin.reflect.KClass

internal val tipoj: Map<String, KClass<out timis>> = mapOf(
        "brodimis" to brodimis::class,
        "girimis" to girimis::class,
        "kamis" to kamis::class,
        "sinemis" to sinemis::class,
        "karimis" to karimis::class,
        "ŝanamis" to ŝanamis::class,
        "milimis" to milimis::class,
        "krumis" to krumis::class,
        "kredimis" to kredimis::class,
        "nomis nevum" to `nomis nevum`::class,
        "teremis" to teremis::class,
        "sanimis" to sanimis::class,
        "talimis" to talimis::class,
        "brenimis" to brenimis::class
)