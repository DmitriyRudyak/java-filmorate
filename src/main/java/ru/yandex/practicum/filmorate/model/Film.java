package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"name"})
@Builder
public class Film {
	Long id;
	String name;
	String description;
	String releaseDate;
	int duration;
}
