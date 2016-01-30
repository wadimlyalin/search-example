package ru.lyalin.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import ru.lyalin.exception.SearchException;
import ru.lyalin.model.ProgrammingLanguage;

import java.util.List;

/**
 * @author Vadim Lyalin
 */
public class ProgrammingLanguageServiceImplTest extends TestCase {
	ProgrammingLanguageServiceImpl programmingLanguageService;

	@Override
	protected void setUp() throws Exception {
		programmingLanguageService = new ProgrammingLanguageServiceImpl();
		programmingLanguageService.afterPropertiesSet();
	}

	/**
	 * Tests search with empty search string
	 */
	public void testSearchEmpty() {
		List<ProgrammingLanguage> programmingLanguages = programmingLanguageService.search("");
		Assert.assertEquals(programmingLanguages.size(), 97);
		ProgrammingLanguage programmingLanguage = programmingLanguages.get(0);
		Assert.assertEquals(programmingLanguage.getName(), "A+");
		Assert.assertEquals(programmingLanguage.getType(), "Array");
		Assert.assertEquals(programmingLanguage.getDesignedBy(), "Arthur Whitney");
	}

	/**
	 * Tests search with search string "Lisp"
	 */
	public void testSearchLisp() {
		String searchString = "Lisp";
		List<ProgrammingLanguage> programmingLanguages = programmingLanguageService.search(searchString);
		Assert.assertEquals(programmingLanguages.size(), 2);
		ProgrammingLanguage programmingLanguage = programmingLanguages.get(0);
		Assert.assertTrue(programmingLanguage.getName().contains(searchString));
	}

	/**
	 * Tests search returned nothing
	 */
	public void testSearchZero() {
		String searchString = "blablabla";
		List<ProgrammingLanguage> programmingLanguages = programmingLanguageService.search(searchString);
		Assert.assertEquals(programmingLanguages.size(), 0);
	}

	/**
	 * Tests search with incorrect search string
	 */
	public void testSearchException() {
		String searchString = "\"test";
		try {
			List<ProgrammingLanguage> programmingLanguages = programmingLanguageService.search(searchString);
			Assert.fail();
		} catch (SearchException e) {
			// do nothing
		}
	}
}
