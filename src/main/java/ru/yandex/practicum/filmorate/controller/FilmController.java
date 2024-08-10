package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

	private final FilmStorage filmStorage;
	private final FilmService filmService;

	@GetMapping
	public Collection<Film> findAll() {
		return filmStorage.findAll();
	}

	// вспомогательный метод для поиска фильма по id
	@GetMapping("/{id}")
	public Optional<Film> findFilmById(@PathVariable("id") Long id) {
		return filmStorage.findFilmById(id);
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		return filmStorage.create(film);
	}

	@PutMapping
	public Film update(@RequestBody Film newFilm) {
		return filmStorage.update(newFilm);
	}

	@PutMapping("/{id}/like/{userId}")
	public void addLike(@PathVariable("id") long id,
						@PathVariable("userId") long userId) {
		filmService.addLike(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	public void deleteLike(@PathVariable("id") long id,
						   @PathVariable("userId") long userId) {
		filmService.deleteLike(id, userId);
	}

	@GetMapping("/popular")
	public List<Film> getPopular(@PathVariable("count") @RequestParam(defaultValue = "10") int count) {
		return filmService.getMostLiked(count);
	}
}
