package fernandez.rafael.cobol.parser_cli;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.List;
import java.util.Optional;


public interface ProcedureRepository extends Neo4jRepository<ProcedureNode, String>{
    
   public Optional<ProcedureNode> findByQualifiedName(String qualifiedName);
}
