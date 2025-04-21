package fernandez.rafael.cobol.parser_cli;

import java.io.File;
import java.util.List;
import java.util.function.Function;
 
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.stereotype.Component;

import io.proleap.cobol.analysis.codeabstract.CobolCodeAbstractRunner; 
import io.proleap.cobol.analysis.codeabstract.impl.CobolCodeAbstractRunnerImpl;
import io.proleap.cobol.analysis.registry.CobolIdRegistry;
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.runner.impl.CobolParserRunnerImpl;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

@Component
public class ConfigurationCLI {

    @Bean
    CommandRegistration hello(){
        return CommandRegistration.builder()
                .command("hello")
                .withTarget()
                .function( ctx -> "Hola Mundo")
                .and()
                .withOption()
                .shortNames('r')
                .defaultValue( " ")
                .and()
                .withOption()
                .longNames("recursive")
                .defaultValue(" ")
                .and()
                .build();
    }
 
    @Bean
    CommandRegistration commandRegistration(final ProcedureRepository repository) {
        return CommandRegistration.builder()
                .command("prog")
                .withTarget()
                .function(done(repository))
                .and()
                .withOption()
                .shortNames('f')
                .required()
                .and()
                .withOption()
                .shortNames('t')
                .required()
                .and()
                .build();
    }

    private final Function<CommandContext, ?> done(final ProcedureRepository repository) {

        return ctx -> {
            try {
                String file = ctx.getOptionValue("f");
                String type = ctx.getOptionValue("t");
                final File inputFile = new File(file);
                final Program program = new CobolParserRunnerImpl().analyzeFile(inputFile,
                        CobolSourceFormatEnum.valueOf(type));
                CobolCodeAbstractRunner<Object> runner = CobolCodeAbstractRunnerImpl.newInstance(Object.class,
                        new Neo4jCodeVisitor(repository));
                final List<Object> result = runner.analyzeProgram(program, new CobolIdRegistry());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return "Done !!!";
        };

    }
}
