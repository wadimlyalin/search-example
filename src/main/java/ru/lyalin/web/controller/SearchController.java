package ru.lyalin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.lyalin.model.ProgrammingLanguage;
import ru.lyalin.service.ProgrammingLanguageService;

import java.util.List;

/**
 * @author Vadim Lyalin
 */
@Controller
public class SearchController {
	@Autowired
	private ProgrammingLanguageService programmingLanguageService;

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	List<ProgrammingLanguage> search(@RequestParam(required = false) String text) {
		return programmingLanguageService.search(text);
	}

	void setProgrammingLanguageService(ProgrammingLanguageService programmingLanguageService) {
		this.programmingLanguageService = programmingLanguageService;
	}
}
