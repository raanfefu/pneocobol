package fernandez.rafael.cobol.parser_cli;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Data;

@Node("Procedure")
@Data
public class ProcedureNode {

    @Id
    private String qualifiedName;

    private String compilationUnitName;
    private String name;
    private int numStatements;
    private int dependents;

    @Relationship(type = "PERFORM", direction = Relationship.Direction.OUTGOING)
    private ProcedureNode parentPerform;

    @Relationship(type = "GOTO", direction = Relationship.Direction.OUTGOING)
    private ProcedureNode parentGoTo;

    public ProcedureNode(String compilationUnitName, String qualifiedName, String name) {
        this.name = name;
        this.compilationUnitName = compilationUnitName;
        this.qualifiedName = qualifiedName;
        this.numStatements = 0;
    }

}
