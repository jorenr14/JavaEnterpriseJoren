package be.ucll.services;

import be.ucll.entities.User;

import java.util.Collection;

public interface UserService {
    Collection<User> findAll();
}
