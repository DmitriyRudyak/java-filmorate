package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler  {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleDuplicatedDataException(final DuplicatedDataException e) {
		log.error("Ошибка с задвоением входных параметров: {}.", e.getMessage());
		return Map.of(
				"error", "Ошибка с входными параметрами.",
				"description", e.getMessage()
		);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleConditionsNotMetException(final ConditionsNotMetException e) {
		log.error("Ошибка с данными параметров: {}.", e.getMessage());
		return Map.of(
				"error", "Ошибка с входными параметрами.",
				"description", e.getMessage()
		);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleNotFoundException(final NotFoundException e) {
		log.error("Ошибка с входными параметрами: {}.", e.getMessage());
		return Map.of(
				"error", "Ошибка с входными параметрами.",
				"description", e.getMessage()
		);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String> handleThrowable(final Throwable e) {
		log.error("Возникла ошибка: {}.", e.getMessage());
		return Map.of(
				"error", "Возникла ошибка сервера.",
				"description", e.getMessage()
		);
	}
}