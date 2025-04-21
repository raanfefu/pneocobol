package io.proleap.cobol.analysis.complete.tandem;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import jakarta.inject.Inject;

import org.dom4j.Document;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.junit.jupiter.api.Test;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.proleap.cobol.analysis.TestBase;
import io.proleap.cobol.analysis.codexml.CobolCodeXmlRunner;
import io.proleap.cobol.analysis.registry.CobolIdRegistry;
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.runner.impl.CobolParserRunnerImpl;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

@MicronautTest
public class ExampleTest extends TestBase {

	@Inject
	private CobolCodeXmlRunner cobolCodeXmlRunner;

	@Test
	public void test() throws Exception {
		final File inputFile = new File("src/test/resources/io/proleap/cobol/analysis/complete/tandem/Example.cbl");
		final Program program = new CobolParserRunnerImpl().analyzeFile(inputFile, CobolSourceFormatEnum.TANDEM);

		final CobolIdRegistry idRegistry = new CobolIdRegistry();
		final List<Document> documents = cobolCodeXmlRunner.analyzeProgram(program, idRegistry);

		final OutputFormat outputFormat = new OutputFormat();
		outputFormat.setTrimText(false);
		outputFormat.setNewlines(true);

		
		documents.forEach( d -> {
			try{
				final StringWriter stringWriter = new StringWriter();
				final HTMLWriter htmlWriter = new HTMLWriter(stringWriter, outputFormat);
				 htmlWriter.write(d);
				htmlWriter.flush();
				htmlWriter.close();
				try (FileWriter fileWriter = new FileWriter("cx.html")) {
					fileWriter.write(stringWriter.toString());
					
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		});
		


		assertNotNull(documents);
		assertFalse(documents.isEmpty());
	}
}