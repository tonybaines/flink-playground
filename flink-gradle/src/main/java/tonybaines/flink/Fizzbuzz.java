package tonybaines.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Fizzbuzz {
  public static void main(String... args) throws Exception {
    // set up the execution environment
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    DataStream<Long> numbers = env.generateSequence(1, 2000);

    final FilterFunction<Long> isFizz = new FilterFunction<Long>() {
      @Override
      public boolean filter(Long n) throws Exception {
        return (n % 3 == 0) && (n % 5 != 0);
      }
    };
    final FilterFunction<Long> isBuzz = new FilterFunction<Long>() {
      @Override
      public boolean filter(Long n) throws Exception {
        return (n % 3 != 0) && (n % 5 == 0);
      }
    };
    final FilterFunction<Long> isFizzBuzz = new FilterFunction<Long>() {
      @Override
      public boolean filter(Long n) throws Exception {
        return (n % 3 == 0) && (n % 5 == 0);
      }
    };

    final MapFunction<Long, Tuple2<Long, String>> toFizz = new MapFunction<Long, Tuple2<Long, String>>() {
      @Override
      public Tuple2 map(Long n) throws Exception {
        return new Tuple2<>(n, "fizz");
      }
    };
    final MapFunction<Long, Tuple2<Long, String>> toBuzz = new MapFunction<Long, Tuple2<Long, String>>() {
      @Override
      public Tuple2 map(Long n) throws Exception {
        return new Tuple2<>(n, "buzz");
      }
    };
    final MapFunction<Long, Tuple2<Long, String>> toFizzBuzz = new MapFunction<Long, Tuple2<Long, String>>() {
      @Override
      public Tuple2 map(Long n) throws Exception {
        return new Tuple2<>(n, "fizz-buzz");
      }
    };


    DataStream<Tuple2<Long, String>> fizzStream = numbers.filter(isFizz).map(toFizz);
    DataStream<Tuple2<Long, String>> buzzStream = numbers.filter(isBuzz).map(toBuzz);
    DataStream<Tuple2<Long, String>> fizzBuzzStream = numbers.filter(isFizzBuzz).map(toFizzBuzz);


    final MapFunction<Tuple2<Long, String>, String> toString = new MapFunction<Tuple2<Long, String>, String>() {
      @Override
      public String map(Tuple2<Long, String> t) throws Exception {
        return t.f0 + " is " + t.f1;
      }
    };

    fizzStream.union(buzzStream).union(fizzBuzzStream)
      .map(toString)
      .print();

    env.execute();
  }
}
