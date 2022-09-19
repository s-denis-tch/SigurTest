package com.sigurtest.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class TimeGeneratorServicesTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void stopGenerating() throws Exception {
        ScheduledAnnotationBeanPostProcessor bean = context.getBean(ScheduledAnnotationBeanPostProcessor.class);
        TimeGeneratorServices schedulerBean = context.getBean(TimeGeneratorServices.class);

        await().atMost(20, SECONDS).untilAsserted(() -> {
            System.err.println("Checking!!!");
            Assertions.assertEquals(false, schedulerBean.getEnabled().get());
        });

        bean.postProcessBeforeDestruction(schedulerBean, "TimeGeneratorServices");
        await().atLeast(3, SECONDS);
        Assertions.assertEquals(false, schedulerBean.getEnabled().get());

        System.err.println("done");
    }

}
