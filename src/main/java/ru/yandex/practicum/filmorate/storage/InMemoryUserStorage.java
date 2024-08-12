package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

	private final Map<Long, User> users = new HashMap<>();
	private final UserValidator validator = new UserValidator();

	@Override
	public Collection<User> findAll() {
		log.info("Список пользователей получен.");
		return users.values();
	}

	@Override
	public Optional<User> findUserById(Long id) {
			return Optional.ofNullable(users.values().stream()
					.filter(user -> user.getId().equals(id))
					.findFirst()
					.orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден.")));
	}

	@Override
	public User create(User user) {
		// проверяем выполнение необходимых условий
		validator.validate(user, users);

		// формируем дополнительные данные
		if (user.getName() == null || user.getName().isBlank()) {
			log.debug("Запись login в пустое поле имени.");
			user.setName(user.getLogin());
		}
		user.setId(getNextId());
		user.setFriends(new HashSet<>());
		// сохраняем новую публикацию в памяти приложения
		users.put(user.getId(), user);
		log.info("Пользователь сохранен.");
		return user;
	}

	@Override
	public User update(User newUser) {
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
