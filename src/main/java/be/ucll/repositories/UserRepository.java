package be.ucll.repositories;

import be.ucll.entities.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();
}
