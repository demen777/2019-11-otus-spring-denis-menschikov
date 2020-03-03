package ru.otus.demen.books.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class User {
    @Id
    @NonNull
    private String username;

    @NonNull
    private String passwordHash;

    @NonNull
    private boolean enabled;

    @ManyToMany
    @JoinTable(name="user_role",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private List<Role> roles;
}
