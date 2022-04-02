package com.example.reactive_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class ReactiveSpringApplicationTests {

    @Test
    void createFlux_just() {
        // just: 리액티브 타입을 생성해주는 메서드
        Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");

        // subscribe: 구독자 추가해주는 메서드
        // 여기에 저장된 람다는 java.util.Consumer 이며, 이것은 reactive stream의 Subscriber 객체를 생성하기 위해 사용됨
        fruitFlux.subscribe(
                f -> System.out.println("Here's some fruit: " + f)
        );

        // StepVerifier: 해당 리액티브 타입을 구독한 다음 스트림을 통해 전달되는 데이터에 대해 assertion 적용
        // -> 해당 스트림이 기대한 대로 완전하게 작동하는지 검사함
        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createFlux_fromArray() {
        String[] fruits = new String[]{"Apple", "Orange", "Grape", "Banana", "Strawberry"};

        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createFlux_fromIterable() {
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Orange");
        fruits.add("Grape");
        fruits.add("Banana");
        fruits.add("Strawberry");

        Flux<String> fruitFlux = Flux.fromIterable(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createFlux_fromStream() {
        Stream<String> fruits = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        Flux<String> fruitFlux = Flux.fromStream(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createFlux_range() {
        // range: 데이터 없이 매번 새 값으로 증가하는 숫자를 방출하는 메서드
        // 1부터 시작하여 5번 증가
        Flux<Integer> intervalFlux = Flux.range(1, 5);

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void createFlux_interval() {
        // interval: 방출되는 시간 간격이나 주기를 지정하는 메서드
        // interval을 통해 만들어진 Flux는 0부터 시작한다.
        // take: interval의 최대값 지정. 이것을 지정하지 않으면 무한정 실행된다.
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1)).take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }
}
