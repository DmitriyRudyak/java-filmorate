package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	private final Map<Long, User> users = new HashMap<>();
	private final UserValidator validator = new UserValidator();

	@GetMapping
	public Collection<User> findAll() {
		log.info("Список пользователей получен.");
		return users.values();
	}

	@PostMapping
	public User create(@RequestBody User user) {
		// проверяем выполнение необходимых условий
		validator.validate(user, users);

		// формируем дополнительные данные
		if (user.getName() == null || user.getName().isBlank()) {
			log.debug("Запись login в пустое поле имени.");
			user.setName(user.getLogin());
		}
		user.setId(getNextId());
		// сохраняем новую публикацию в памяти приложения
		users.put(user.getId(), user);
		log.info("Пользователь сохранен.");
		return user;
	}

	@PutMapping
	public User update(@RequestBody User newUser) {
		// проверяем необходимые условия
		if (newUser.getId() == null) {
			log.error("Пустое поле id.");
			throw new NotFoundException("Id должен быть указан.");
		}
		users.values().stream()
				.filter(user -> newUser.getEmail() != null)
				.filter(user -> newUser.getEmail().equals(user.getEmail()))
				.forEach(user -> {
					log.error("Данный имейл уже используется.");
					throw new DuplicatedDataException("Этот имейл уже используется."); });

		// проверяем выполнение необходимых условий
		validator.validate(newUser, users);

		if (users.containsKey(newUser.getId())) {
			User oldUser = users.get(newUser.getId());

			if (newUser.getEmail() != null) {
				log.debug("Перезапись email в поле.");
				oldUser.setEmail(newUser.getEmail());
			}

			if (newUser.getLogin() != null) {
				log.debug("Перезапись login в поле.");
				oldUser.setLogin(newUser.getLogin());
			}

			if (newUser.getName() != null) {
				log.debug("Перезапись name в поле.");
				oldUser.setName(newUser.getName());
			}

			if (newUser.getBirthday() != null) {
				log.debug("Перезапись birthday в поле.");
				oldUser.setBirthday(newUser.getBirthday());
			}
			log.info("Пользователь обновлен.");
			return oldUser;
		}
		log.error("Отсутствует пользователь с данным id.");
		throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден.");
	}

	// вспомогательный метод для генерации идентификатора нового поста
	private long getNextId() {
		long currentMaxId = users.keySet()
				.stream()
				.mapToLong(id -> id)
				.max()
				.orElse(0);
		log.debug("Сгенерирован новый id.");
		return ++currentMaxId;
	}
}
