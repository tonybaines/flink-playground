package tonybaines.flink

// Need this import for Flink implicits
import java.util.Properties

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer09, FlinkKafkaProducer09}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object KafkaFlink {
  def main(args: Array[String]) {
    // set up the execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "test")

    val source: DataStream[String] = env.addSource(new FlinkKafkaConsumer09[String]("in", new SimpleStringSchema(), properties))
    source.print()

    val sink: DataStreamSink[String] = source.map(_.toUpperCase).addSink(new FlinkKafkaProducer09[String]("localhost:9092", "out", new SimpleStringSchema()))

    env.execute()

  }

}
