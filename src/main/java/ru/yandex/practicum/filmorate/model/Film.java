package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(of = {"name"})
@Builder
public class Film {
	protected Long id;
	protected String name;
	protected String description;
	protected String releaseDate;
	protected int duration;
	private Set<Long> likes;
}
