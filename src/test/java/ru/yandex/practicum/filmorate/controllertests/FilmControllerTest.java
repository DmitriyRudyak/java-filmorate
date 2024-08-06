package ru.yandex.practicum.filmorate.controllertests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

@SpringBootTest
public class FilmControllerTest {
	private final FilmController filmController = new FilmController();

	@Test
	void shouldReturnFilmCollection() {
		Film film1 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		filmController.create(film1);

		Assertions.assertEquals(1, filmController.findAll().size());
	}

	@Test
	void shouldCreateNewFilm() {
		Film film1 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();

		Assertions.assertDoesNotThrow(() -> filmController.create(film1));
	}

	@Test
	void shouldNotCreateNewFilm() {
		Film film1 = Film.builder()
				.name("")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();

		Assertions.assertThrows(NotFoundException.class,() -> filmController.create(film1));
		Assertions.assertEquals(0, filmController.findAll().size());
	}

	@Test
	void shouldThrowNameErrors() {
		Film film1 = Film.builder()
				.name("")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		Film film2 = Film.builder()
				.name("name2")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		filmController.create(film2);
		Film film3 = Film.builder()
				.name("name2")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();

		Assertions.assertThrows(NotFoundException.class,() -> filmController.create(film1));
		Assertions.assertThrows(DuplicatedDataException.class,() -> filmController.create(film3));
	}

	@Test
	void shouldThrowDescriptionErrors() {
		Film film1 = Film.builder()
				.name("name1")
				.description(new String(new char[201]).replace('\0', '*'))
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		Film film2 = Film.builder()
				.name("name2")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();

		Assertions.assertThrows(ConditionsNotMetException.class,() -> filmController.create(film1));
		Assertions.assertThrows(NotFoundException.class,() -> filmController.create(film2));
	}

	@Test
	void shouldThrowReleaseDateErrors() {
		Film film1 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("1010-10-10")
				.duration(210)
				.build();
		Film film2 = Film.builder()
				.name("film2")
				.description("description")
				.releaseDate("")
				.duration(210)
				.build();
		Film film3 = Film.builder()
				.name("film3")
				.description("description")
				.duration(210)
				.build();
		Film film4 = Film.builder()
				.name("film4")
				.description("description")
				.releaseDate("10102010")
				.duration(210)
				.build();

		Assertions.assertThrows(ConditionsNotMetException.class,() -> filmController.create(film1));
		Assertions.assertThrows(NotFoundException.class,() -> filmController.create(film2));
		Assertions.assertThrows(NotFoundException.class,() -> filmController.create(film3));
		Assertions.assertThrows(ConditionsNotMetException.class,() -> filmController.create(film4));
	}

	@Test
	void shouldThrowDurationErrors() {
		Film film1 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(0)
				.build();
		Film film2 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(-1)
				.build();
		Film film3 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.build();

		Assertions.assertThrows(ConditionsNotMetException.class,() -> filmController.create(film1));
		Assertions.assertThrows(ConditionsNotMetException.class,() -> filmController.create(film2));
		Assertions.assertThrows(ConditionsNotMetException.class,() -> filmController.create(film3));
	}

	@Test
	void shouldUpdateFilms() {
		Film film1 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		filmController.create(film1);
		Film film2 = Film.builder()
				.id(1L)
				.name("film2")
				.description("description2")
				.releaseDate("2010-10-10")
				.duration(220)
				.build();

		Assertions.assertDoesNotThrow(() -> filmController.update(film2));

		Film filmFromController = (filmController.findAll()).iterator().next();

		Assertions.assertEquals(film2.getName(), filmFromController.getName());
		Assertions.assertEquals(film1.getId(), filmFromController.getId());
	}

	@Test
	void shouldNotUpdateFilms() {
		Film film1 = Film.builder()
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		filmController.create(film1);
		Film film2 = Film.builder()
				.name("film2")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		Film film3 = Film.builder()
				.id(1L)
				.name("film1")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();
		Film film4 = Film.builder()
				.id(10L)
				.name("film10")
				.description("description")
				.releaseDate("2010-10-10")
				.duration(210)
				.build();


		Assertions.assertThrows(NotFoundException.class,() -> filmController.update(film2));
		Assertions.assertThrows(DuplicatedDataException.class,() -> filmController.update(film3));
		Assertions.assertThrows(NotFoundException.class,() -> filmController.update(film4));
	}
}
