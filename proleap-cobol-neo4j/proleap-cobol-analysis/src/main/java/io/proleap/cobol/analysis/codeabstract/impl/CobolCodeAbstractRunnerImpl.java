package io.proleap.cobol.analysis.codeabstract.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.proleap.cobol.analysis.codeabstract.AbstractCodeVisitor;
import io.proleap.cobol.analysis.codeabstract.CobolCodeAbstractRunner;
import io.proleap.cobol.analysis.registry.CobolIdRegistry;
import io.proleap.cobol.asg.metamodel.CompilationUnit;
import io.proleap.cobol.asg.metamodel.Program;

public class CobolCodeAbstractRunnerImpl<T> implements CobolCodeAbstractRunner<T> {

    AbstractCodeVisitor<T> visitor;

    public CobolCodeAbstractRunnerImpl ( Class<T> clazz,AbstractCodeVisitor<T> visitor){
        this.visitor = visitor;
    }

	public static <T> CobolCodeAbstractRunnerImpl<T> newInstance(Class<T> clazz, AbstractCodeVisitor<T> visitor){
		return  new CobolCodeAbstractRunnerImpl(clazz, visitor);
	}

	@Override
	public List<T> analyzeProgram(final Program program, final CobolIdRegistry idRegistry) throws IOException {
		final List<T> result = new ArrayList<T>();

		for (final CompilationUnit compilationUnit : program.getCompilationUnits()) {
			final T document = analyzeCompilationUnit( program, compilationUnit, idRegistry);
			result.add(document);
		}
		return result;
	}

	@Override
	public T analyzeCompilationUnit(final Program program, final CompilationUnit compilationUnit, final CobolIdRegistry idRegistry)
			throws IOException {
		visitor.setup(program,compilationUnit,idRegistry);
		T result = visitor.createResult();
		visitor.visit(compilationUnit.getCtx());
		return result;
	}

   }
