package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class DemoController {

    @GetMapping("synchronize")
    void synchronize() {
        long start = System.currentTimeMillis();
        System.out.println("Start");
        String result1 = task1();
        String result2 = task2();
        System.out.println("Finish: result1=" + result1 + " result2=" + result2);
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
    }

    @GetMapping("webflux")
    Mono<Void> webFlux() {
        long start = System.currentTimeMillis();
        System.out.println("Start");

        Mono<String> task1 = Mono.fromSupplier(() -> {
            return task1();
        })
                .subscribeOn(Schedulers.boundedElastic());

        Mono<String> task2 = Mono.fromSupplier(() -> {
            return task2();
        }).subscribeOn(Schedulers.boundedElastic());

        return Mono.zip(task1, task2)
                .doOnNext((result) -> {

                    String result1 = result.getT1();
                    String result2 = result.getT2();

                    System.out.println("Finish: result1=" + result1 + " result2=" + result2);
                    System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
                }).then();
    }

    String task1() {
        System.out.println("Start task 1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("End task 1");

        return "Result 1";
    }

    String task2() {

        System.out.println("Start task 2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("End task 2");

        return "Result 2";
    }
}
