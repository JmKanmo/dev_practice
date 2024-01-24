package com.util;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 최신 Java (21)에서 어떤 기능들이 추가 되었는지, 문법 등을 실습 해보도록 한다.
 */
public class RecentJavaTest {
    public static void main(String[] args) {
        test2();
    }
    public static void test1() {
        Type type = Type.GOOD;

        String str = switch (type) {
            case GOOD:
                yield "good";
            case BAD:
                yield "bad";
            case SOSO:
                yield "soso";
        };

        String b = """
                This
                is
                text block
                """;

        System.out.println(str);
        System.out.println(b);

        Object obj = "fdfdd";

        if (obj instanceof String s) {
            System.out.println(s);
        }

        /**
         * 관련 링크: https://openjdk.org/jeps/431
         * LinkedHashSet | LinkedHashMap
         */
        SequencedSet<Integer> sequencedSet = new LinkedHashSet<>();
        sequencedSet.addLast(255);
        sequencedSet.addLast(195);

        for (Integer s : sequencedSet) {
            System.out.println(s);
        }

        /**
         * 가상 스레드
         * https://findstar.pe.kr/2023/04/17/java-virtual-threads-1/
         */

        // 방법 1
        Thread.startVirtualThread(() -> {
            System.out.println("Hello Virtual Thread - way1");
        });

        // 방법 2
        Thread.ofVirtual().start(() -> {
            System.out.println("Hello Virtual Thread - way2");
        });

        // 방법 3 (이름 지정)
        Thread.ofVirtual().name("virtual-thread-way3").start(() -> {
            System.out.println("virtual-thread-way3");
        });

        // 방법 4 가상스레드전용 ExecutorService
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            // virtual thread - way4
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    System.out.println("virtual thread" + "[ " + finalI + "]" + "way4");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 3초간 대기
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2() {
        while (true) {
            long start = System.currentTimeMillis();
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                // 10만개의 Virtual Thread 실행
                for (int i = 0; i < 100_000; i++) {
                    executor.submit(() -> {
                        Thread.sleep(Duration.ofSeconds(2));
                        return null;
                    });
                }

            }
            long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
        }
    }
    private enum Type {
        GOOD("good"),
        BAD("bad"),
        SOSO("soso");

        private String str;

        Type(String str) {
            this.str = str;
        }
    }
}
