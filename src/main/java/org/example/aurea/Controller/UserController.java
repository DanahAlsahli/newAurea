package org.example.aurea.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.aurea.Api.ApiResponse;
import org.example.aurea.Model.User;
import org.example.aurea.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
        User savedUser = userService.addUser(user);

        return ResponseEntity.status(201).body(savedUser);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {

        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {

        User user = userService.getUserById(id);
        return ResponseEntity.status(200).body(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid User user) {
        userService.updateUser(id, user);

        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

        userService.deleteUser(id);

        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }
}
