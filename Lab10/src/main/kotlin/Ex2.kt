import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun producer(broker: Channel<Int>, brokerNumber: Int) {
    repeat(brokerNumber) {
        delay(500)
        val value = (0..1000).random()

        println("Producer sent value: $value")
        broker.send(value)
    }
}
suspend fun consumer(broker: Channel<Int>, brokerNumber: Int) {
    repeat(brokerNumber) {
       println("Consumer received value: ${broker.receive()}")
    }
}

suspend fun processor(from: Channel<Int>, to: Channel<Int>) {
    while (true) {
        to.send(from.receive())
    }
}

fun main() {
    val brokerNumber = 10
    val channels = List(brokerNumber) { Channel<Int>()}

    runBlocking {
        for (num in 1 until brokerNumber) {
            launch { processor(channels[num - 1], channels[num]) }
        }

        launch { producer(channels[0], brokerNumber) }
        launch{ consumer(channels[brokerNumber - 1], brokerNumber) }
    }
}
