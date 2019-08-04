package xyz.trankvila.menteia.datumo

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import xyz.trankvila.menteia.vojoj.AliriloRespondo

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

val SlackChallengeAdatper = moshi.adapter(SlackChallenge::class.java)
val SlackEventAdapter = moshi.adapter(SlackEvent::class.java)
val SlackMessageEventAdapter = moshi.adapter(MessageEvent::class.java)
val SlackMessageAdapter = moshi.adapter(SlackMessage::class.java)

val ADMAccessTokenResponseAdapter = moshi.adapter(AccessTokenResponse::class.java)
val ADMRequestAdapter = moshi.adapter(ADMRequest::class.java)

val ForecastResponseAdapter = moshi.adapter(ForecastResponse::class.java)
val ObservationResponseAdapter = moshi.adapter(ObservationResponse::class.java)

val AliriloRespondoAdapter = moshi.adapter(AliriloRespondo::class.java)
val IdleClockResponseAdapter = moshi.adapter(IdleClockResponse::class.java)