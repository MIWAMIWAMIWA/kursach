package ua.lviv.iot.algo.part1.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"ua.lviv.iot.algo.part1.controller",
        "ua.lviv.iot.algo.part1.service",
        "ua.lviv.iot.algo.part1.mappers"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
