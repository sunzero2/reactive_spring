package com.example.reactive_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MappingFluxTests {
    @Test
    public void map() {
        // Flux.map() : 각 항목이 소스 Flux로부터 발행될 때 동기적으로 매핑이 수행됨
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]);
                });

        StepVerifier.create(playerFlux)
                .expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen"))
                .expectNext(new Player("Steve", "Kerr"))
                .verifyComplete();
    }

    @Test
    public void flatMap() {
        // Flux.flatMap() : 각 객체를 새로운 Mono 혹은 Flux로 매핑. 해당 Mono 혹은 Flux들의 결과는 하나의  새로운 Flux가 됨.
        // Flux.subscribeOn(): 각 구독이 병렬 스레드로 수행되어야 함을 의미
        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] split = p.split("\\s");
                            return new Player(split[0], split[1]);
                        })
                .subscribeOn(Schedulers.parallel()));

        List<Player> players = Arrays.asList(new Player("Michael", "Jordan"), new Player("Scottie", "Pippen"), new Player("Steve", "Kerr"));

        StepVerifier.create(playerFlux)
                .expectNextMatches(players::contains)
                .expectNextMatches(players::contains)
                .expectNextMatches(players::contains)
                .verifyComplete();
    }


    public class Player {
        private final String firstName;
        private final String lastName;

        public Player(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return firstName + " " + lastName;
        }
    }
}
