package com.pilot.project.configs;

import com.pilot.project.entities.Hotel;
import com.pilot.project.utils.HotelItemProcessor;
import com.pilot.project.utils.JobCompletionNotificationListener;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;

@Configuration
public class BatchConfig {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public BatchConfig(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Hotel> reader(@Value("#{jobParameters[fullPathFileName]}") String pathFile ){
        return new FlatFileItemReaderBuilder<Hotel>()
                .name("hotelItemReader")
                .resource(new FileSystemResource(new File(pathFile)))
                .delimited()
                .names("hotelId","hotelName", "hotelAddress", "hotelCity")
                .targetType(Hotel.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public HotelItemProcessor processor(){
        return new HotelItemProcessor();
    }

    @Bean
    public JpaItemWriter<Hotel> writer(){
        JpaItemWriter<Hotel> writer=new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Job hotelJob(JobRepository hotelJobRepository, Step hotelStep, JobCompletionNotificationListener listener){
        return new JobBuilder("hotelJob", hotelJobRepository)
                .listener(listener)
                .start(hotelStep)
                .build();
    }
    @Bean
    public Step hotelStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          JpaItemWriter<Hotel> writer,
                          HotelItemProcessor processor,
                          FlatFileItemReader<Hotel> reader){
        return new StepBuilder("hotelStep", jobRepository)
                .<Hotel, Hotel>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(5)
                .skip(Exception.class)
                .build();
    }


}
