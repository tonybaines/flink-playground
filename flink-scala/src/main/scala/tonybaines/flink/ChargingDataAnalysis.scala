package tonybaines.flink

// Need this import for Flink implicits
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import scala.io.Source

object ChargingDataAnalysis {
  def main(args: Array[String]) {
    // set up the execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val csv: List[Array[String]] = Source.fromFile("src/main/resources/charging-data.csv")
      .getLines()
      .map(_.split(","))
      .toList

    val recordStream: DataStream[Array[String]] = env.fromCollection(csv)

    val sum: DataStream[(Array[String], Int)] = recordStream
      .map { r => (r, 1) }
      .keyBy(r => r._1)
      .sum(1)

    print(sum)

    env.execute()
  }
}