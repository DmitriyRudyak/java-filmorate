package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

	private final Map<Long, Film> films = new HashMap<>();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
	private final LocalDate filmStartDate = LocalDate.parse("28.12.1895", formatter);

	@GetMapping
	public Collection<Film> findAll() {
		log.info("Список фильмов получен.");
		return films.values();
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		// проверяем выполнение необходимых условий
		if (film.getName() == null || film.getName().isBlank()) {
			log.error("Пустое поле name.");
			throw new NotFoundException("Название фильма должно быть указано.");
		}

		if (films.containsValue(film)) {
			log.error("Конфликт одинаковых фильмов.");
			throw new DuplicatedDataException("Этот фильм уже находится в базе.");
		}

		try {
			if (film.getDescription().length() > 200) {
				log.error("Превышение лимита символов в описании.");
				throw new ConditionsNotMetException("Описание фильма превышает 200 символов.");
			}
		} catch (NullPointerException e) {
			log.error("Описание отсутствует.");
			throw new NotFoundException("Описание фильма отсутствует.");
		}

		try {
			if (LocalDate.parse(film.getReleaseDate(), formatter).isBefore(filmStartDate)) {
				log.error("Указанная дата релиза фильма ошибочна.");
				throw new ConditionsNotMetException("Укажите верную дату релиза фильма.");
			}
		} catch (DateTimeParseException | NullPointerException e) {
			log.error("Дата релиза фильма отсутствует.");
			throw new NotFoundException("Укажите дату релиза фильма.");
		}

		if (film.getDuration() <= 0) {
			log.error("Продолжительность фильма ошибочна");
			throw new ConditionsNotMetException("Продолжительность фильма должна быть указана верно.");
		}

		// формируем дополнительные данные
		film.setId(getNextId());
		// сохраняем новую публикацию в памяти приложения
		films.put(film.getId(), film);
		log.info("Фильм сохранен.");
		return film;
	}

	@PutMapping
	public Film update(@RequestBody Film newFilm) {
		// проверяем необходимые условия
		if (newFilm.getId() == null) {
			log.error("Пустое поле id.");
			throw new NotFoundException("Id должен быть указан");
		}
		films.values().stream()
				.filter(user -> newFilm.getName() != null)
				.filter(user -> newFilm.getName().equals(user.getName()))
				.forEach(user -> {
					log.error("Данный фильм уже сохранен.");
					throw new DuplicatedDataException("Данный фильм уже сохранен."); });

		if (films.containsKey(newFilm.getId())) {
			Film oldFilm = films.get(newFilm.getId());

			if (newFilm.getName() != null) {
				log.debug("Перезапись name в поле.");
				oldFilm.setName(newFilm.getName());
			}

			if (newFilm.getDescription() != null) {
				log.debug("Перезапись description в поле.");
				oldFilm.setDescription(newFilm.getDescription());
			}

			if (newFilm.getReleaseDate() != null) {
				log.debug("Перезапись releaseDate в поле.");
				oldFilm.setReleaseDate(newFilm.getReleaseDate());
			}

			if (newFilm.getDuration() > 0) {
				log.debug("Перезапись duration в поле.");
				oldFilm.setDuration(newFilm.getDuration());
			}
			log.info("Фильм обновлен.");
			return oldFilm;
		}
		log.error("Отсутствует фильм с данным id.");
		throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
	}

	// вспомогательный метод для генерации идентификатора нового поста
	private long getNextId() {
		long currentMaxId = films.keySet()
				.stream()
				.mapToLong(id -> id)
				.max()
				.orElse(0);
		log.debug("Сгенерирован новый id.");
		return ++currentMaxId;
	}
}
