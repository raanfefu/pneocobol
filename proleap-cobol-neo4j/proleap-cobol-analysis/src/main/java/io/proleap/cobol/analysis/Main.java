package io.proleap.cobol.analysis;

import java.io.File;
import java.util.Arrays;
import java.util.List;
 
 
import io.proleap.cobol.analysis.codeabstract.CobolCodeAbstractRunner;
import io.proleap.cobol.analysis.codeabstract.ConcreteCodeVisitor;
 
import io.proleap.cobol.analysis.codeabstract.impl.CobolCodeAbstractRunnerImpl; 
import io.proleap.cobol.analysis.registry.CobolIdRegistry;
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.runner.impl.CobolParserRunnerImpl;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class Main {
    public static void main(String[] args) { 
		try {

		
			final File inputFile = new File(
					"/Users/rafaelfernandez/proleap-modificated/proleap-cobol-neo4j/proleap-cobol-analysis/src/test/resources/io/proleap/cobol/analysis/complete/tandem/Example.cbl");
			final Program program = new CobolParserRunnerImpl().analyzeFile(inputFile, CobolSourceFormatEnum.TANDEM);
	
			CobolCodeAbstractRunner<Object> runner = CobolCodeAbstractRunnerImpl.newInstance(Object.class,new ConcreteCodeVisitor());
			final List<Object> result = runner.analyzeProgram(program, new CobolIdRegistry());

			// });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
