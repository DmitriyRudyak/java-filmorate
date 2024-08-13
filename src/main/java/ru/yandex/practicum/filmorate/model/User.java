package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(of = {"email"})
@Builder
public class User {
	protected Set<Long> friends;
	protected Long id;
	protected String email;
	protected String login;
	protected String name;
	protected String birthday;
}
