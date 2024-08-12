package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

	private final FilmService filmService;

	@GetMapping
	public Collection<Film> findAll() {
		return filmService.findAll();
	}

	// вспомогательный метод для поиска фильма по id
	@GetMapping("/{id}")
	public Optional<Film> findFilmById(@PathVariable Long id) {
		return filmService.findFilmById(id);
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		return filmService.create(film);
	}

	@PutMapping
	public Film update(@RequestBody Film newFilm) {
		return filmService.update(newFilm);
	}

	@PutMapping("/{id}/like/{userId}")
	public void addLike(@PathVariable long id,
						@PathVariable long userId) {
		filmService.addLike(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	public void deleteLike(@PathVariable long id,
						   @PathVariable long userId) {
		filmService.deleteLike(id, userId);
	}

	@GetMapping("/popular")
	public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
		return filmService.getMostLiked(count);
	}
}
