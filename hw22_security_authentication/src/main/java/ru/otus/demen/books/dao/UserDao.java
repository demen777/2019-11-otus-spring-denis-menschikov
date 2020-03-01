package ru.otus.demen.books.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.demen.books.model.User;

public interface UserDao extends JpaRepository<User, String> {
}
