import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.selects.select

suspend fun producer(brokers: ArrayList<Channel<Int>>) {
    while (true) {
        delay(1000)
        select {
            val brokerNumber = brokers.size
            val randomNumber = (0 until brokerNumber).random()
            val broker = brokers[randomNumber]
            broker.onSend(randomNumber) {println("Sent to channel $randomNumber")}
        }
    }
}

suspend fun consume(brokers: ArrayList<Channel<Int>>) {
    while (true) {
        select {
            brokers.forEach { receiveChannel -> receiveChannel.onReceive { channel -> println("Received from channel $channel") } }
        }
    }
}

suspend fun broke(fromProducer: Channel<Int>, toConsumer: Channel<Int>) {
    while (true) {
        val channel = fromProducer.receive()
        toConsumer.send(channel)
    }
}

fun main() {
    val brokerNumber = 10
    val prodChannels = ArrayList<Channel<Int>>()
    val consChannels = ArrayList<Channel<Int>>()

    for (num in 1..brokerNumber) {
        prodChannels.add(Channel())
        consChannels.add(Channel())
    }

    runBlocking {
        for (num in 0 until brokerNumber) {
            launch { broke(prodChannels[num], consChannels[num]) }
        }
        launch { producer(prodChannels) }
        launch { consume(consChannels) }
    }
}
