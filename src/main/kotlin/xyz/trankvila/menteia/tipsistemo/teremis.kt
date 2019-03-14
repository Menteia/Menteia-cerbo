package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.runBlocking
import java.time.Duration

abstract class teremis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    protected abstract val _daŭro: Duration

    override suspend fun _valuigi(): Duration {
        return _daŭro
    }
}

class trinis(morem: girimis): teremis(morem) {
    override val _daŭro = runBlocking {
        Duration.ofSeconds(morem._valuigi().numeratorAsLong)
    }

}

class nires(morem: girimis): teremis(morem) {
    override val _daŭro = runBlocking {
        Duration.ofMinutes(morem._valuigi().numeratorAsLong)
    }
}

class gomos(morem: girimis): teremis(morem) {
    override val _daŭro = runBlocking {
        Duration.ofHours(morem._valuigi().numeratorAsLong)
    }
}