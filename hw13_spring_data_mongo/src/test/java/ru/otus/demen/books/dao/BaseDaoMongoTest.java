package ru.otus.demen.books.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;


@DataMongoTest
@ComponentScan(basePackages = {"ru.otus.demen.books.dao", "ru.otus.demen.books.bee"})
class BaseDaoMongoTest {
    @Autowired
    MongoTemplate mongoTemplate;
}
