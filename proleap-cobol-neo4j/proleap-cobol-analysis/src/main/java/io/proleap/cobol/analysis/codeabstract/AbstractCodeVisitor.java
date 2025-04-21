package io.proleap.cobol.analysis.codeabstract;

import org.dom4j.Document;

import io.proleap.cobol.CobolBaseVisitor;
import io.proleap.cobol.analysis.registry.CobolIdRegistry;
import io.proleap.cobol.asg.metamodel.CompilationUnit;
import io.proleap.cobol.asg.metamodel.Program;

public  abstract class AbstractCodeVisitor<T> extends CobolBaseVisitor<Boolean> {
    
    protected Program program;
    protected CobolIdRegistry idRegistry;
    protected  CompilationUnit compilationUnit;

    public void setup(Program program, CompilationUnit compilationUnit, CobolIdRegistry idRegistry ){
        this.compilationUnit = compilationUnit;
        this.idRegistry = idRegistry;
        this.program = program;

    }

    public abstract  T createResult();
     
}
