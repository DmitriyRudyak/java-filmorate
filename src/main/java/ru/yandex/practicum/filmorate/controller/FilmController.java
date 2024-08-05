package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

	private final Map<Long, Film> films = new HashMap<>();
	private final FilmValidator validator = new FilmValidator();

	@GetMapping
	public Collection<Film> findAll() {
		log.info("Список фильмов получен.");
		return films.values();
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		// проверяем выполнение необходимых условий
		validator.validate(film, films);

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

		// проверяем выполнение необходимых условий
		validator.validate(newFilm, films);

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
