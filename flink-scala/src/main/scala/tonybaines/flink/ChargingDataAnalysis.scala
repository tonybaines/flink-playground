package tonybaines.flink

// Need this import for Flink implicits
import org.apache.flink.api.scala._
import java.text.SimpleDateFormat

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.json4s.native.JsonMethods._
import org.json4s.{JValue, _}

object ChargingDataAnalysis {

  case class Id(id: String)

  case class LastUpdated(lastUpdated: String)

  def main(args: Array[String]) {
    // set up the execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    /*
        val csv: List[Array[String]] = Source.fromFile("src/main/resources/charging-data.csv")
          .getLines()
          .map(_.split(","))
          .toList

        val recordStream: DataStream[Array[String]] = env.fromCollection(csv)*/

    val json = parse(org.json4s.file2JsonInput(new java.io.File("src/main/resources/charging-data.json")))

    val devices = (json \\ "ChargeDevice").children

    val recordStream: DataStream[JValue] = env.fromCollection(devices)

    implicit val formats = new DefaultFormats with Serializable {
      override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }

    val stream: DataStream[(String, java.util.Date)] = recordStream
      .keyBy(r => r \\ "RecordLastUpdated")
      .map(r => ((r \\ "ChargeDeviceId"), r \\ "RecordLastUpdated"))
      .map(d => (d._1.extract[String], d._2.extract[java.util.Date]))

    stream.print()

    env.execute()
  }
}