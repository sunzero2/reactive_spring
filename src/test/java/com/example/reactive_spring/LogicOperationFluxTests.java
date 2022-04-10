package com.example.reactive_spring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LogicOperationFluxTests {
    @Test
    public void all() {
        // all(): 모든 메시지가 조건을 충족하는지 확인함
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
        Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));
        StepVerifier.create(hasAMono)
                .expectNext(true)
                .verifyComplete();

        Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));
        StepVerifier.create(hasKMono)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    public void any() {
        // any(): 단 하나의 메시지라도 조건을 충족하는지 확인함
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
        Mono<Boolean> hasTMono = animalFlux.any(a -> a.contains("t"));
        StepVerifier.create(hasTMono)
                .expectNext(true)
                .verifyComplete();

        Mono<Boolean> hasZMono = animalFlux.any(a -> a.contains("z"));
        StepVerifier.create(hasZMono)
                .expectNext(false)
                .verifyComplete();
    }
}
