package fernandez.rafael.cobol.parser_cli.builder;

import java.util.Optional;

import fernandez.rafael.cobol.parser_cli.ProcedureNode;
import io.proleap.cobol.analysis.util.NamingUtils;
import io.proleap.cobol.asg.metamodel.CompilationUnit;
import io.proleap.cobol.asg.metamodel.call.Call;
import io.proleap.cobol.asg.metamodel.call.ProcedureCall;

public class ProcedureNodeFromCallBuilder {

    private CompilationUnit compilationUnit;
    private Call procedureCall;
    private Optional<ProcedureNode> cache;
    private ProcedureNode parentPerform;
    private ProcedureNode parentGoTo;

    private ProcedureNodeFromCallBuilder(Call procedureCall, CompilationUnit compilationUnit) {
        this.procedureCall = procedureCall;
        this.compilationUnit = compilationUnit;
        cache = Optional.empty();
    }

    private ProcedureNodeFromCallBuilder(Call procedureCall, CompilationUnit compilationUnit, ProcedureNode parentPerform,ProcedureNode parentGoTo) {
        this(procedureCall,compilationUnit);
        this.parentPerform = parentPerform;
        this.parentGoTo = parentGoTo;
    }

    public ProcedureNodeFromCallBuilder procedureNode(Optional<ProcedureNode> cache) {
        this.cache = cache;
        return this;
    }

    public ProcedureNodeFromCallBuilder parentPerform(ProcedureNode cache) {
        this.parentPerform = cache;
        return this;
    }

    public ProcedureNodeFromCallBuilder parentGoTo(ProcedureNode cache) {
        this.parentGoTo = cache;
        return this;
    }

    public static ProcedureNodeFromCallBuilder newInstance(ProcedureCall procedureCall,
            CompilationUnit compilationUnit) {
        return new ProcedureNodeFromCallBuilder(procedureCall, compilationUnit);
    }
    public static ProcedureNodeFromCallBuilder newInstance(Call procedureCall,
            CompilationUnit compilationUnit, ProcedureNode parentPerform, ProcedureNode parentGoTo) {
        return new ProcedureNodeFromCallBuilder(procedureCall, compilationUnit, parentPerform,parentGoTo);
    }

    public String getQualifiedName() {
        return NamingUtils.determineFullQualifiedName(procedureCall.getCtx(), compilationUnit);
    }

    public ProcedureNode build() {
        ProcedureNode result = null;
        if(!cache.isEmpty()){
            String cqn = cache.get().getQualifiedName();
            String pqn = parentPerform.getQualifiedName();
            if (cqn.equalsIgnoreCase(pqn)){
                System.out.println("************************ cyclic reference ************************");
                return null;
            }

        }
        if (cache.isEmpty()) {
            result = new ProcedureNode(compilationUnit.getName(),getQualifiedName(), getQualifiedName());
            result.setParentPerform(parentPerform);
            result.setParentPerform(parentGoTo);
        } else {
            result = cache.get();
            result.setParentPerform(parentPerform);
            result.setParentPerform(parentGoTo);
        }
        
        return result;
    }

}
