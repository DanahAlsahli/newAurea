package org.example.aurea.Service;

import lombok.RequiredArgsConstructor;
import org.example.aurea.Api.ApiException;
import org.example.aurea.Model.User;
import org.example.aurea.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user) {

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(Integer id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException("User not found"));
    }

    public void updateUser(
            Integer id,
            User user) {

        User oldUser =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        "User not found"));

        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());

        userRepository.save(oldUser);
    }

    public void deleteUser(Integer id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        "User not found"));

        userRepository.delete(user);
    }
}
