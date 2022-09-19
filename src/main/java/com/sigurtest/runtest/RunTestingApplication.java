package com.sigurtest.runtest;

import com.sigurtest.services.TimeGeneratorServices;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootApplication
@EnableScheduling
@Order(value = 2)
public class RunTestingApplication implements CommandLineRunner {

    private final ApplicationContext context;
    public RunTestingApplication(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(RunTestingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ScheduledAnnotationBeanPostProcessor bean = context.getBean(ScheduledAnnotationBeanPostProcessor.class);
        TimeGeneratorServices schedulerBean = context.getBean(TimeGeneratorServices.class);

        await().atMost(370, SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(false, schedulerBean.getEnabled().get());
            System.err.println("Successful.");
        });
        bean.postProcessBeforeDestruction(schedulerBean, "TimeGeneratorServices");
    }
}
