package fernandez.rafael.cobol.parser_cli.builder;

import org.antlr.v4.runtime.ParserRuleContext;

import fernandez.rafael.cobol.parser_cli.ProcedureNode;
import io.proleap.cobol.CobolParser.GoToStatementContext;
import io.proleap.cobol.CobolParser.ParagraphContext;
import io.proleap.cobol.analysis.util.NamingUtils;
import io.proleap.cobol.asg.metamodel.ASGElement;
import io.proleap.cobol.asg.metamodel.CompilationUnit;
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.metamodel.procedure.Paragraph;
import io.proleap.cobol.asg.metamodel.procedure.gotostmt.GoToStatement;
import io.proleap.cobol.asg.metamodel.procedure.gotostmt.Simple;
import io.proleap.cobol.asg.metamodel.registry.ASGElementRegistry;

public class ProcedureNodeBuilder {

    private ParagraphContext paragraphContext;

    private CompilationUnit compilationUnit;
    private Program program;
    private ParserRuleContext formType;

    private ProcedureNode parent;

    private ProcedureNodeBuilder(Program program, CompilationUnit compilationUnit, ParserRuleContext formType) {
        this.program = program;
        this.compilationUnit = compilationUnit;
        this.formType = formType;
    }

    public static ProcedureNodeBuilder newInstanceFromParagraphContext(Program program, CompilationUnit compilationUnit,
            ParagraphContext pctx) {
        return new ProcedureNodeBuilder(program, compilationUnit, pctx);
    }

    public static ProcedureNodeBuilder newInstanceFromGoToStatementContext(Program program,
            CompilationUnit compilationUnit,
            GoToStatementContext pctx) {
        return new ProcedureNodeBuilder(program, compilationUnit, pctx);
    }

    public String getQualifiedName() {
        return NamingUtils.determineFullQualifiedName(getASGElement((ParserRuleContext) formType).getCtx(),
                compilationUnit);
    }

    private ASGElement getASGElement(final ParserRuleContext ctx) {
        final Program program = compilationUnit.getProgram();
        final ASGElementRegistry asgElementRegistry = program.getASGElementRegistry();
        return asgElementRegistry.getASGElement(ctx);
    }

    public ProcedureNode build() {
        if (formType instanceof ParagraphContext) {
            return fromParagraphBuild();
        } else if (formType instanceof GoToStatementContext) {
            return fromGoToStatementBuild();
        } else {
            throw new RuntimeException("Not Support Type Builder");
        }

    }

    private ProcedureNode fromGoToStatementBuild() {
        GoToStatement goToStatement = ((GoToStatement) getASGElement(formType));
        GoToStatement.GoToType type = goToStatement.getGoToType();

        if (GoToStatement.GoToType.SIMPLE == type) {
            Simple s = goToStatement.getSimple();
        } else if (GoToStatement.GoToType.DEPENDING_ON == type) {
            
        } else {
            throw new RuntimeException("Not Support Go To Statement Type");
        }
        
        // ProcedureNode result = new ProcedureNode(compilationUnit.getName(),
        //         getQualifiedName(),
        //         getQualifiedName(),
        //         parent);
        return null;
    }

    private ProcedureNode fromParagraphBuild() {
        Paragraph paragraph = ((Paragraph) getASGElement(formType));
        ProcedureNode result =  new ProcedureNode(compilationUnit.getName(),
                getQualifiedName(),
                paragraph.getName());
        if (paragraph.getStatements() != null && paragraph.getStatements().size() > 0) {
            result.setNumStatements(paragraph.getStatements().size());
        }
        if (paragraph.getCalls() != null & paragraph.getCalls().size() > 0) {
            result.setDependents(paragraph.getCalls().size());
        }
        return result;
    }
}
