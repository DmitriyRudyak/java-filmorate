package ru.yandex.practicum.filmorate.controllertests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor
public class UserControllerTests {
	private final InMemoryUserStorage userStorage = new InMemoryUserStorage();
	private final UserController userController = new UserController(userStorage, new UserService(userStorage));

	@Test
	void shouldReturnUserCollection() {
		User user1 = User.builder()
				.email("email@1")
				.login("login1")
				.name("name1")
				.birthday("2010-10-10")
				.build();
		userController.create(user1);

		Assertions.assertEquals(1, userController.findAll().size());
	}

	@Test
	void shouldCreateNewUser() {
		User user1 = User.builder()
				.email("email@1")
				.login("login1")
				.name("name1")
				.birthday("2010-10-10")
				.build();

		Assertions.assertDoesNotThrow(() -> userController.create(user1));
	}

	@Test
	void shouldNotCreateNewUser() {
		User user1 = User.builder()
				.email("")
				.login("login1")
				.name("name1")
				.birthday("2010-10-10")
				.build();

		Assertions.assertThrows(NotFoundException.class,() -> userController.create(user1));
		Assertions.assertEquals(0, userController.findAll().size());
	}

	@Test
	void shouldThrowEmailErrors() {
		User user1 = User.builder()
				.email("")
				.login("login1")
				.name("name1")
				.birthday("2010-10-10")
				.build();
		User user2 = User.builder()
				.email("email")
				.login("login2")
				.name("name2")
				.birthday("2010-10-10")
				.build();
		User user3 = User.builder()
				.email("email@3")
				.login("login3")
				.name("name3")
				.birthday("2010-10-10")
				.build();
		userController.create(user3);
		User user4 = User.builder()
				.email("email@3")
				.login("login4")
				.name("name4")
				.birthday("2010-10-10")
				.build();

		Assertions.assertThrowsExactly(NotFoundException.class,() -> userController.create(user1));
		Assertions.assertThrowsExactly(ConditionsNotMetException.class,() -> userController.create(user2));
		Assertions.assertThrowsExactly(DuplicatedDataException.class,() -> userController.create(user4));
	}

	@Test
	void shouldThrowLoginErrors() {
		User user1 = User.builder()
				.email("email@1")
				.login("")
				.name("name1")
				.birthday("2010-10-10")
				.build();
		User user2 = User.builder()
				.email("email@2")
				.login("login 1")
				.name("name2")
				.birthday("2010-10-10")
				.build();

		Assertions.assertThrowsExactly(NotFoundException.class,() -> userController.create(user1));
		Assertions.assertThrowsExactly(ConditionsNotMetException.class,() -> userController.create(user2));
	}

	@Test
	void shouldThrowBirthdayErrors() {
		User user1 = User.builder()
				.email("email@1")
				.login("login1")
				.name("name1")
				.birthday("3010-10-10")
				.build();
		User user2 = User.builder()
				.email("email@2")
				.login("login2")
				.name("name2")
				.birthday("")
				.build();

		Assertions.assertThrowsExactly(ConditionsNotMetException.class,() -> userController.create(user1));
		Assertions.assertThrowsExactly(NotFoundException.class,() -> userController.create(user2));
	}

	@Test
	void shouldCreateNameAsLogin() {
		User user1 = User.builder()
				.email("email@1")
				.login("login1")
				.name("")
				.birthday("2010-10-10")
				.build();
		userController.create(user1);
		User userFromController = (userController.findAll()).iterator().next();

		Assertions.assertEquals(userFromController.getName(), userFromController.getLogin());
	}

	@Test
	void shouldUpdateNewUser() {
		User user1 = User.builder()
				.email("email@1")
				.login("login1")
				.name("name1")
				.birthday("2010-10-10")
				.build();
		userController.create(user1);
		User user2 = User.builder()
				.id(1L)
				.email("email@2")
				.login("login2")
				.name("name2")
				.birthday("2010-10-10")
				.build();


		Assertions.assertDoesNotThrow(() -> userController.update(user2));

		User userFromController = (userController.findAll()).iterator().next();

		Assertions.assertEquals(user2.getName(), userFromController.getName());
		Assertions.assertEquals(user1.getId(), userFromController.getId());
	}

	@Test
	void shouldNotUpdateNewUser() {
		User user1 = User.builder()
				.email("email@1")
				.login("login1")
				.name("name1")
				.birthday("2010-10-10")
				.build();
		userController.create(user1);
		User user2 = User.builder()
				.email("email@2")
				.login("login2")
				.name("name2")
				.birthday("2010-10-10")
				.build();
		User user3 = User.builder()
				.id(1L)
				.email("email@1")
				.login("login2")
				.name("name2")
				.birthday("2010-10-10")
				.build();
		User user4 = User.builder()
				.id(10L)
				.email("email@4")
				.login("login4")
				.name("name4")
				.birthday("2010-10-10")
				.build();

		Assertions.assertThrowsExactly(NotFoundException.class,() -> userController.update(user2));
		Assertions.assertThrowsExactly(DuplicatedDataException.class,() -> userController.update(user3));
		Assertions.assertThrowsExactly(NotFoundException.class,() -> userController.update(user4));
	}

	@Test
	void shouldReturnUserByID() {
		User user1 = User.builder()
				.email("email@1")
				.login("login1")
				.name("name1")
				.birthday("2010-10-10")
				.build();
		userController.create(user1);

		Assertions.assertEquals(Optional.of(user1), userController.findUserById(1L));
	}

}
