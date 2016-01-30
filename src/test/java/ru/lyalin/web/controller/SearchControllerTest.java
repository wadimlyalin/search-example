package ru.lyalin.web.controller;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import ru.lyalin.model.ProgrammingLanguage;
import ru.lyalin.service.ProgrammingLanguageService;

import java.util.ArrayList;

/**
 * @author Vadim Lyalin
 */
public class SearchControllerTest extends TestCase {
	private SearchController searchController;

	@Override
	protected void setUp() throws Exception {
		searchController = new SearchController();
	}

	public void testSearch() {
		String searchString = "Lisp";
		ArrayList<ProgrammingLanguage> programmingLanguages = new ArrayList<ProgrammingLanguage>();

		ProgrammingLanguageService programmingLanguageService = EasyMock.createMock(ProgrammingLanguageService.class);
		EasyMock.expect(programmingLanguageService.search(searchString)).andReturn(programmingLanguages);
		searchController.setProgrammingLanguageService(programmingLanguageService);
		EasyMock.replay(programmingLanguageService);

		Assert.assertSame(programmingLanguages, searchController.search(searchString));
	}
}
