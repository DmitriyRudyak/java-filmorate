package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

	private final Map<Long, Film> films = new HashMap<>();
	private final FilmValidator validator = new FilmValidator();

	@Override
	public Collection<Film> findAll() {
		log.info("Список фильмов получен.");
		return films.values();
	}

	@Override
	public Optional<Film> findFilmById(Long id) {
		try {
			log.info("Фильм получен.");
			return films.values().stream()
					.filter(film -> film.getId().equals(id))
					.findFirst();
		} catch (RuntimeException e) {
			log.error("Фильм не найден.");
			throw new NotFoundException("Фильм с id " + id + " не найден.");
		}
	}

	@Override
	public Film create(Film film) {
		// проверяем выполнение необходимых условий
		validator.validate(film, films);

		// формируем дополнительные данные
		film.setId(getNextId());
		// сохраняем новую публикацию в памяти приложения
		films.put(film.getId(), film);
		log.info("Фильм сохранен.");
		return film;
	}

	@Override
	public Film update(Film newFilm) {
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
