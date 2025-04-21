package io.proleap.cobol.analysis.codeabstract;

import java.io.IOException;
import java.util.List;

import io.proleap.cobol.analysis.registry.CobolIdRegistry;
import io.proleap.cobol.asg.metamodel.CompilationUnit;
import io.proleap.cobol.asg.metamodel.Program;

public interface CobolCodeAbstractRunner<T> {
 
     T analyzeCompilationUnit(Program program, CompilationUnit compilationUnit, CobolIdRegistry idRegistry ) throws IOException;
	List<T> analyzeProgram(Program program, CobolIdRegistry idRegistry) throws IOException;

}
