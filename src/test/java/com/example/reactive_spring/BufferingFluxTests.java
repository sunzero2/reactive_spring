package com.example.reactive_spring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BufferingFluxTests {
    @Test
    public void buffer() {
        // buffer(): 따로따로 있던 Flux 요소를 하나의 List Flux로 만들 수 있음
        // 매개변수에 숫자를 넣으면 해당 크기만큼의 List가 만들어지는거고, 매개변수에 아무것도 넣지 않으면 Flux의 요소 크기만큼의 List가 만들어짐
        // 예제는 매개변수에 3을 넣었으니 크기가 3인 리스트가 만들어지고, 리스트 1에는 apple, orange, banana 리스트 2에는 kiwi, strawberry가 들어감
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");
        Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(Arrays.asList("apple", "orange", "banana"))
                .expectNext(Arrays.asList("kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    public void buffer2() {
        // buffer를 flatMap과 같이 사용하면 각 리스트 컬렉션을 병행으로 처리할 수 있음
        Flux.just("apple", "orange", "banana", "kiwi", "strawberry")
                .buffer(3)
                .flatMap(x ->
                        Flux.fromIterable(x)
                                .map(String::toUpperCase)
                                .subscribeOn(Schedulers.parallel())
                                .log()
                ).subscribe();
    }

    @Test
    public void collectList() {
        // collectList(): Flux를 List로 만들어 Mono로 변환해줌
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");
        Mono<List<String>> fruitListMono = fruitFlux.collectList();

        StepVerifier.create(fruitListMono)
                .expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    public void collectMap() {
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
        Mono<Map<Character, String>> animalMapMono = animalFlux.collectMap(a -> a.charAt(0));

        StepVerifier.create(animalMapMono)
                .expectNextMatches(map ->
                    map.size() == 3 && map.get('a').equals("aardvark")
                            && map.get('e').equals("eagle")
                            && map.get('k').equals("kangaroo"))
                .verifyComplete();
    }
}
