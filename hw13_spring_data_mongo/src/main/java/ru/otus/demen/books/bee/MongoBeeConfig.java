package ru.otus.demen.books.bee;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.demen.books.bee.changelog.DatabaseChangelog;

@Configuration public class MongoBeeConfig {
    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Mongobee mongobee(Environment environment) {
        return new Mongobee(mongoClient)
                .setDbName(environment.getProperty("spring.data.mongodb.database"))
                .setChangeLogsScanPackage(DatabaseChangelog.class.getPackage().getName())
                .setMongoTemplate(mongoTemplate)
                .setSpringEnvironment(environment);
    }
}
