package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserStorage userStorage;
	private final UserService userService;

	@GetMapping
	public Collection<User> findAll() {
		return userStorage.findAll();
	}

	// вспомогательный метод для поиска пользователя по id
	@GetMapping("/{id}")
	public Optional<User> findUserById(@PathVariable("id") Long id) {
		return userStorage.findUserById(id);
	}

	@PostMapping
	public User create(@RequestBody User user) {
		return userStorage.create(user);
	}

	@PutMapping
	public User update(@RequestBody User newUser) {
		return userStorage.update(newUser);
	}

	@PutMapping("/{id}/friends/{friendId}")
	public void addFriend(@PathVariable("id") long id,
						  @PathVariable("friendId") long friendId) {
		userService.addFriend(id, friendId);
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	public void deleteFriend(@PathVariable("id") long id,
							 @PathVariable("friendId") long friendId) {
		userService.deleteFriend(id, friendId);
	}

	@GetMapping("/{id}/friends")
	public List<User> getUserFriends(@PathVariable("id") long id) {
		return userService.getUserFriends(id);
	}

	@GetMapping("/{id}/friends/common/{otherId}")
	public List<User> getCommonFriends(@PathVariable("id") long id,
									   @PathVariable("otherId") long otherId) {
		return userService.getCommonFriends(id, otherId);
	}
}
