package tonybaines.flink

// Need this import for Flink implicits
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import tonybaines.flink.data.Csv

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

    val stream: DataStream[(String, String)] = recordStream
      .filter(r => r.length >= Csv.lastUpdated)
      .keyBy(r => r(Csv.lastUpdated))
      .map(r => (r(Csv.chargeDeviceID), r(Csv.lastUpdated)))

    stream.print()

    env.execute()
  }
}