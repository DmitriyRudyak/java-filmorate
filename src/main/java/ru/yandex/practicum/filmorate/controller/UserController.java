package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public Collection<User> findAll() {
		return userService.findAll();
	}

	// вспомогательный метод для поиска пользователя по id
	@GetMapping("/{id}")
	public Optional<User> findUserById(@PathVariable Long id) {
		return userService.findUserById(id);
	}

	@PostMapping
	public User create(@RequestBody User user) {
		return userService.create(user);
	}

	@PutMapping
	public User update(@RequestBody User newUser) {
		return userService.update(newUser);
	}

	@PutMapping("/{id}/friends/{friendId}")
	public void addFriend(@PathVariable long id,
						  @PathVariable long friendId) {
		userService.addFriend(id, friendId);
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	public void deleteFriend(@PathVariable long id,
							 @PathVariable long friendId) {
		userService.deleteFriend(id, friendId);
	}

	@GetMapping("/{id}/friends")
	public List<User> getUserFriends(@PathVariable long id) {
		return userService.getUserFriends(id);
	}

	@GetMapping("/{id}/friends/common/{otherId}")
	public List<User> getCommonFriends(@PathVariable long id,
									   @PathVariable long otherId) {
		return userService.getCommonFriends(id, otherId);
	}
}
