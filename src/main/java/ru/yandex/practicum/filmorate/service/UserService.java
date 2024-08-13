package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserStorage userStorage;

	public void addFriend(Long userId, Long friendId) {
		User user = userStorage.findUserById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

		User friend = userStorage.findUserById(friendId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id " + friendId + " не найден"));

		user.getFriends().add(friendId);
		friend.getFriends().add(userId);
		log.info("Пользователь с id {} добавил в друзья пользователя с id {}.", userId, friendId);
	}

	public void deleteFriend(Long userId, Long friendId) {
		User user = userStorage.findUserById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
		if (user.getFriends() == null) {
			throw new ConditionsNotMetException("У пользователя " + userId + " нет друзей");
		}

		User friend = userStorage.findUserById(friendId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id " + friendId + " не найден"));
		if (friend.getFriends() == null) {
			throw new ConditionsNotMetException("У пользователя с id " + friendId + " нет друзей");
		}

		user.getFriends().remove(friendId);
		friend.getFriends().remove(userId);
		log.info("Пользователь с id {} удалил из друзей пользователя с id {}.", userId, friendId);
	}

	public List<User> getUserFriends(Long userId) {
		User user = userStorage.findUserById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
		if (user.getFriends() == null) {
			throw new ConditionsNotMetException("У пользователя с id " + userId + " нет друзей");
		}
		return user.getFriends().stream()
				.map(id -> userStorage.findUserById(id)
						.orElseThrow(() -> new NotFoundException("Пользователя с id " + id + " не существует")))
				.collect(Collectors.toList());
	}

	public List<User> getCommonFriends(Long userId, Long otherId) {
		Set<Long> userFriendsSet = userStorage.findUserById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"))
				.getFriends();
		Set<Long> otherUserFriendsSet = userStorage.findUserById(otherId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id " + otherId + " не найден"))
				.getFriends();

		Set<Long> common = new HashSet<>(userFriendsSet);
		common.retainAll(otherUserFriendsSet);

		return common.stream()
				.map(id -> userStorage.findUserById(id)
						.orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден")))
				.collect(Collectors.toList());
	}

	public Collection<User> findAll() {
		return userStorage.findAll();
	}

	public Optional<User> findUserById(Long id) {
		return userStorage.findUserById(id);
	}

	public User create(User user) {
		return userStorage.create(user);
	}

	public User update(User newUser) {
		return userStorage.update(newUser);
	}
}