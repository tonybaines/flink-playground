package tonybaines.flink

// Need this import for Flink implicits
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object FizzBuzzStreams {
  def main(args: Array[String]) {
    // set up the execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val numbers: DataStream[Long] = env.generateSequence(1, 2000)

    val fizzStream = numbers
      .filter(n => (n % 3 == 0) && (n % 5 != 0))
      .map((_,"fizz"))
    val buzzStream = numbers
      .filter(n => (n % 3 != 0) && (n % 5 == 0))
      .map((_,"buzz"))
    val fizzBuzzStream = numbers
      .filter(n => (n % 3 == 0) && (n % 5 == 0))
      .map((_,"fizz-buzz"))


    fizzStream.union(buzzStream).union(fizzBuzzStream)
      .map(_ match {case (n,s) => n+" is "+s})
      .print()

    env.execute()
  }
}
