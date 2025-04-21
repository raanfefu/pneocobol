package fernandez.rafael.cobol.parser_cli;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import fernandez.rafael.cobol.parser_cli.builder.ProcedureNodeBuilder;
import fernandez.rafael.cobol.parser_cli.builder.ProcedureNodeFromCallBuilder;
import io.proleap.cobol.CobolParser.GoToStatementContext; 
import io.proleap.cobol.CobolParser.ParagraphContext;
import io.proleap.cobol.analysis.codeabstract.AbstractCodeVisitor;
import io.proleap.cobol.analysis.util.NamingUtils;
import io.proleap.cobol.asg.metamodel.ASGElement;
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.metamodel.procedure.Paragraph;
import io.proleap.cobol.asg.metamodel.registry.ASGElementRegistry;

public class Neo4jCodeVisitor extends AbstractCodeVisitor<Object> {

    ProcedureRepository repository;

    public Neo4jCodeVisitor(ProcedureRepository repository) {
        this.repository = repository;
    }

    @Override
    public Object createResult() {
        return null;
    }

    // @Override
    // public Boolean visitGoToStatement(final GoToStatementContext ctx) {
    //     GoToStatement goToStatement = ((GoToStatement) getASGElement(ctx));
    //     GoToStatement.GoToType type = goToStatement.getGoToType();
    //     if (GoToStatement.GoToType.SIMPLE == type) {
    //         ProcedureNodeBuilder pnBuilder = ProcedureNodeBuilder.newInstanceFromGoToStatementContext(program,
    //                 compilationUnit, ctx);
    //         ProcedureNode parentNode = pnBuilder.build();
    //         Optional<ProcedureNode> cacheParentNode = repository.findByQualifiedName(pnBuilder.getQualifiedName());
    //         if (cacheParentNode.isEmpty()) {
    //             parentNode = pnBuilder.build();
    //             parentNode = repository.save(parentNode);
    //             System.out.println("NO CACHE:" + pnBuilder.getQualifiedName());
    //         } else {
    //             parentNode = cacheParentNode.get();
    //             System.out.println(" CACHE:" + pnBuilder.getQualifiedName());
    //         }
    //         Call call = goToStatement.getSimple().getProcedureCall();
    //         //ProcedureNodeBuilder mbuilder = ProcedureNodeBuilder.
            
             
    //     }

    //     return visitChildren(ctx);
    // }

    private String naming(ParseTree pt){
        return NamingUtils.determineFullQualifiedName(pt,compilationUnit);
    }

    @Override
    public Boolean visitParagraph(final ParagraphContext ctx) {

        String naming = NamingUtils.determineFullQualifiedName(getASGElement((ParserRuleContext) ctx).getCtx(),
                compilationUnit);
        System.out.println(naming);
        ((Paragraph) getASGElement(ctx)).getCalls()
        .stream()
        .map(c -> naming(c.getCtx()))
        .map(c -> { 
            System.out.println(".  "+c);
            return c;
        })
        .toList();
        System.out.print("\n");
        // ProcedureNodeBuilder pnBuilder = ProcedureNodeBuilder.newInstanceFromParagraphContext(program, compilationUnit,
        //         ctx);
        // ProcedureNode parentNode = null;
        // Optional<ProcedureNode> cacheParentNode = repository.findByQualifiedName(pnBuilder.getQualifiedName());
        // if (cacheParentNode.isEmpty()) {
        //     parentNode = pnBuilder.build();
        //     repository.save(parentNode);
        // } else {
        //     parentNode = cacheParentNode.get();
        // }

        // final ProcedureNode finalParentNode = parentNode;
        // ((Paragraph) getASGElement(ctx)).getCalls()
        //         .stream()
        //         .map(pc -> ProcedureNodeFromCallBuilder.newInstance(pc, compilationUnit))
        //         .map(  pc ->  pc.parentPerform(finalParentNode))
        //         .map(c ->  c.procedureNode(repository.findByQualifiedName(c.getQualifiedName())))
        //         .map( pc-> pc.build())
        //         .peek(c-> System.out.print(c))
        //         .filter(c -> c != null)
        //         .map(c -> repository.save(c))
        //         .toList();
        return visitChildren(ctx);
    }

    private ASGElement getASGElement(final ParserRuleContext ctx) {
        final Program program = compilationUnit.getProgram();
        final ASGElementRegistry asgElementRegistry = program.getASGElementRegistry();
        return asgElementRegistry.getASGElement(ctx);
    }

    // @SuppressWarnings("unchecked")
    // protected List<String> getCallerNames(final List<?> calls) {
    // return ((List<Call>) calls).stream()
    // .map(call -> NamingUtils.determineFullQualifiedName(call.getCtx(),
    // compilationUnit))
    // .collect(Collectors.toList());
    // }

}
