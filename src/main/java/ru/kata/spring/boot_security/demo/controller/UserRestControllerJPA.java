
package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositorey.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/users")
@Validated // Для валидации параметров в методах (например, @RequestParam)
public class UserRestControllerJPA {

        private final UserService userService;
        private final RoleService roleService;
        private final UserRepository userRepository;

        @Autowired
        public UserRestControllerJPA(UserService userService, RoleService roleService, UserRepository userRepository) {
            this.userService = userService;
            this.roleService = roleService;
            this.userRepository = userRepository;
        }

        // ✅ Получить список всех пользователей
        @GetMapping
        public List<User> getAllUsers() {
            return userService.listUsers();
        }

        // ✅ Получить пользователя по ID
        @GetMapping("/{id}")
        public ResponseEntity<User> getUser(@PathVariable Long id) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.notFound().build();

            }
        }

        // ✅ Создать нового пользователя
        @PostMapping
        public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }

        // ✅ Обновить пользователя
        @PutMapping("/{id}")
        public ResponseEntity<User> updateUser(@PathVariable Long id,
                                               @RequestBody User updatedUser) {
            updatedUser.setId(id);
            User savedUser = userRepository.save(updatedUser);
            return ResponseEntity.ok(savedUser);
        }

        // ✅ Удалить пользователя
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/current")
        public ResponseEntity<User> getCurrentUser(Principal principal) {
            User user = userRepository.findByUsername(principal.getName());
            return ResponseEntity.ok(user);
        }
    }