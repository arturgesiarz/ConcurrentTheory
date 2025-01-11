import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.selects.select

suspend fun producer(brokers: List<Channel<Int>>) {
    repeat(20) {
        delay(500)
        val value = (0..1000).random()
        select {
            brokers.forEach {sendChannel -> sendChannel.onSend(value) { println("Producer sent value: $value") } }
        }
    }
}

suspend fun consumer(brokers: List<Channel<Int>>) {
    while (true) {
        select {
            brokers.forEach {receiveChannel -> receiveChannel.onReceive { value -> println("Consumer received value: $value") } }
        }
    }
}

suspend fun broke(fromProducer: Channel<Int>, toConsumer: Channel<Int>) {
    while (true) {
        val portion = fromProducer.receive()
        toConsumer.send(portion)
    }
}

fun main() {
    val brokerNumber = 10
    val channels = List(brokerNumber) {Pair(Channel<Int>(), Channel<Int>())}

    runBlocking {
        channels.forEach {(fromProducer, toConsumer) -> launch { broke(fromProducer, toConsumer) }}
        launch { producer(channels.map { it.first }) }
        launch { consumer(channels.map { it.second }) }
    }
}
