package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Slf4j
public class FilmValidator {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final LocalDate filmStartDate = LocalDate.parse("1895-12-28", formatter);
	private static final int MAX_DESCRIPTION = 200;

	public void validate(Film film, Map<Long, Film> films) {
		if (film.getName() == null || film.getName().isBlank()) {
			log.error("Пустое поле name.");
			throw new NotFoundException("Название фильма должно быть указано.");
		}

		if (films.containsValue(film)) {
			log.error("Конфликт одинаковых фильмов.");
			throw new DuplicatedDataException("Этот фильм уже находится в базе.");
		}

		if (film.getDescription() == null || film.getDescription().isBlank()) {
			log.error("Описание отсутствует.");
			throw new NotFoundException("Описание фильма отсутствует.");
		} else if (film.getDescription().length() > MAX_DESCRIPTION) {
			log.error("Превышение лимита символов в описании.");
			throw new ConditionsNotMetException("Описание фильма превышает 200 символов.");
		}

		try {
			if (film.getReleaseDate() == null || film.getReleaseDate().isBlank()) {
				log.error("Дата релиза фильма отсутствует.");
				throw new NotFoundException("Укажите дату релиза фильма.");
			} else if (LocalDate.parse(film.getReleaseDate(), formatter).isBefore(filmStartDate)) {
				log.error("Указанная дата релиза фильма неверна.");
				throw new ConditionsNotMetException("Укажите верную дату релиза фильма.");
			}
		} catch (DateTimeParseException e) {
			log.error("Указан неверный формат даты релиза фильма.");
			throw new ConditionsNotMetException("Укажите дату релиза фильма в верном формате.");
		}

		if (film.getDuration() <= 0) {
			log.error("Продолжительность фильма ошибочна");
			throw new ConditionsNotMetException("Продолжительность фильма должна быть указана верно.");
		}
	}
}

