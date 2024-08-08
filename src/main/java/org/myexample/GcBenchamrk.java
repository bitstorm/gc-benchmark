package org.myexample;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(jvmArgsAppend =  {"-Xms10g", "-Xmx10g", "-Xmn7g" }, value = 1)
@State(Scope.Benchmark)
public class GcBenchamrk {

    public static class GcData {
        String[] arr;

        public GcData() {
            try {
                setup();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public void setup() throws IOException, InterruptedException {
            System.out.println("Setup");
            arr = new String[1048576];
            for (int c = 0; c < 1048576; c++) {
                arr[c] = ("Value" + c);
            }

            Collections.shuffle(Arrays.asList(arr), new Random(0xBAD_BEE));

            System.gc();
         }
    }

    GcData gcData = new GcData();

    @Benchmark
    public void iterateBlackHole(Blackhole blackhole) {


        for (String val : gcData.arr) {
            blackhole.consume( val.hashCode());
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(GcBenchamrk.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

}
