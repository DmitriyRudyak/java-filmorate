package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public class UserValidator {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public void validate(User user, Map<Long, User> users) {
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			log.error("Пустое поле email.");
			throw new NotFoundException("Имейл должен быть указан.");
		} else if (!Arrays.asList(user.getEmail().split("")).contains("@")) {
			log.error("Отсутствует '@'.");
			throw new ConditionsNotMetException("Имейл указан некорректно. Отсутствует '@'.");
		}

		if (users.containsValue(user)) {
			log.error("Конфликт одинаковых имейлов.");
			throw new DuplicatedDataException("Этот имейл уже используется.");
		}

		if (user.getLogin() == null || user.getLogin().isBlank()) {
			log.error("Пустое поле login.");
			throw new NotFoundException("Логин должен быть указан.");
		} else if (user.getLogin().contains(" ")) {
			log.error("Присутствуют пробелы в поле login.");
			throw new ConditionsNotMetException("Логин указан некорректно. Присутствуют пробелы.");
		}

		try {
			if (user.getBirthday() == null || user.getBirthday().isBlank()) {
				log.error("Не указана дата дня рождения.");
				throw new NotFoundException("Укажите дату рождения.");
			} else if (LocalDate.parse(user.getBirthday(), formatter).isAfter(LocalDate.now())) {
				log.error("Неверно указана дата дня рождения.");
				throw new ConditionsNotMetException("Укажите верную дату рождения.");
			}
		} catch (DateTimeParseException e) {
			log.error("Дата рождения указана в неверном формате.");
			throw new NotFoundException("Укажите дату рождения в верном формате.");
		}
	}
}
