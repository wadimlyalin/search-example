package ru.lyalin.service;

import ru.lyalin.exception.SearchException;
import ru.lyalin.model.ProgrammingLanguage;

import java.util.List;

/**
 * @author Vadim Lyalin
 */
public interface ProgrammingLanguageService {
	List<ProgrammingLanguage> search(String text) throws SearchException;
}
