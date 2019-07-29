package tutorial1

import java.util.Properties

import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

object ProducerDemoWithCallbackAndKeysScala extends App {

  val logger = LoggerFactory.getLogger(ProducerDemoWithCallbackAndKeysScala.getClass)

  val bootStrapServers = "127.0.0.1:9092"

  val properties = new Properties()
  properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers)
  properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  //StringSerializer.class is mentioned in scala as classOf[StringSerializer]
  properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

  val producer : KafkaProducer[String, String] = new KafkaProducer[String, String](properties)



  for (i <- 1 to 10) {
    val topic = "first_topic"
    val value = s"Hello world ${i}"
    val key = s"id_${i}"
    val producerRecord : ProducerRecord[String, String] = new ProducerRecord[String, String](topic, key, value)
    producer.send(producerRecord, new Callback {
      override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
        if (exception == null) {
          logger.info("Topic : " + metadata.topic() + "\n" +
            "Partition : " + metadata.partition() + "\n" +
            "Offset : " + metadata.offset() + "\n" +
            "Timestamp : " + metadata.timestamp() +"\n"
          )
        } else {
          logger.error("Error while producing " + exception)
        }
      }
    })
  }

  producer.flush()

  producer.close()

}
