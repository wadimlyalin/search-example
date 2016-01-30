package ru.lyalin.service;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.lyalin.exception.SearchException;
import ru.lyalin.model.ProgrammingLanguage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Lyalin
 */
@Component
public class ProgrammingLanguageServiceImpl implements ProgrammingLanguageService, InitializingBean {
	private static final String DATA_FILE_NAME = "data.json";
	private RAMDirectory idx;

	@Override
	public void afterPropertiesSet() throws Exception {
		buildIndex(read());
	}

	private void buildIndex(ProgrammingLanguage[] programmingLanguages) {
		idx = new RAMDirectory();
		IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
		try {
			IndexWriter writer = new IndexWriter(idx, iwc);
			for (ProgrammingLanguage programmingLanguage : programmingLanguages) {
				Document document = new Document();
				document.add(new TextField("name", programmingLanguage.getName(), Field.Store.YES));
				document.add(new TextField("type", programmingLanguage.getType(), Field.Store.YES));
				document.add(new TextField("designedBy", programmingLanguage.getDesignedBy(), Field.Store.YES));
				// create content filed to index all fields of programming language
				document.add(new TextField("content", programmingLanguage.getDesignedBy() + " " + programmingLanguage.getType()
						+ " " + programmingLanguage.getName(), Field.Store.YES));
				writer.addDocument(document);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ProgrammingLanguage[] read() {
		return read(getClass().getClassLoader().getResourceAsStream(DATA_FILE_NAME));
	}

	private ProgrammingLanguage[] read(InputStream is) throws IllegalArgumentException {
		try {
			return new ObjectMapper().readValue(is, new ProgrammingLanguage[0].getClass());
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Convert document to programming language
	 * @param doc document
	 * @return programming language
	 */
	private ProgrammingLanguage toProgrammingLanguage(Document doc) {
		ProgrammingLanguage programmingLanguage = new ProgrammingLanguage();
		programmingLanguage.setName(doc.get("name"));
		programmingLanguage.setType(doc.get("type"));
		programmingLanguage.setDesignedBy(doc.get("designedBy"));
		return programmingLanguage;
	}

	/**
	 * Search for programming language
	 * @param text search string
	 * @return programming languages
	 */
	public List<ProgrammingLanguage> search(String text) throws SearchException {
		try {
			List<ProgrammingLanguage> programmingLanguages = new ArrayList<ProgrammingLanguage>();
			// create searcher
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(idx));
			if(StringUtils.isEmpty(text)) {
				// search string is empty. Return all programming languages
				IndexReader indexReader = searcher.getIndexReader();
				for(int i = 0; i < indexReader.maxDoc(); i++) {
					programmingLanguages.add(toProgrammingLanguage(indexReader.document(i)));
				}
			} else {
				// search string is not empty. Do search.
				QueryParser parser = new QueryParser("content", new StandardAnalyzer());
				Query query = parser.parse(text);
				TopDocs results = searcher.search(query, 100);
				ScoreDoc[] hits = results.scoreDocs;

				for(int i = 0; i < results.totalHits; i++) {
					programmingLanguages.add(toProgrammingLanguage(searcher.doc(hits[i].doc)));
				}
			}
			return programmingLanguages;
		} catch (Exception e) {
			throw new SearchException();
		}
	}
}
