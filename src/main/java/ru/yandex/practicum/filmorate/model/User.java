package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"email"})
@Builder
public class User {
	Long id;
	String email;
	String login;
	String name;
	String birthday;
}
