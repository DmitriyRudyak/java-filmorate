package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"email"})
@Builder
public class User {
	protected Long id;
	protected String email;
	protected String login;
	protected String name;
	protected String birthday;
}
