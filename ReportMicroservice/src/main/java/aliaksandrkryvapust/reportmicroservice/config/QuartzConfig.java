package aliaksandrkryvapust.reportmicroservice.config;

import aliaksandrkryvapust.reportmicroservice.job.ReportLoadJob;
import lombok.NonNull;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {
    private final ApplicationContext applicationContext;

    public QuartzConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ReportLoadJob.class);
        return factoryBean;
    }

    @Bean
    SimpleTriggerFactoryBean simpleTriggerFactoryBean(JobDetail jobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(60000);
        factoryBean.setRepeatInterval(300000);
        return factoryBean;
    }

    @Bean
    SpringBeanJobFactory springBeanJobFactory(){
        return new SpringBeanJobFactory(){
            @Override
            protected @NonNull Object createJobInstance(final @NonNull TriggerFiredBundle bundle) throws Exception{
                final Object job = super.createJobInstance(bundle);
                applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
                return job;
            }
        };
    }

    @Bean
    SchedulerFactoryBean schedulerFactoryBean(SpringBeanJobFactory beanJobFactory, Trigger trigger){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setAutoStartup(true);
        factoryBean.setWaitForJobsToCompleteOnShutdown(true);
        factoryBean.setTriggers(trigger);
        beanJobFactory.setApplicationContext(applicationContext);
        factoryBean.setJobFactory(beanJobFactory);
        return factoryBean;
    }
}
