import static org.codehaus.groovy.control.CompilePhase.CONVERSION

import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.classgen.GeneratorContext
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.customizers.CompilationCustomizer

import java.nio.charset.StandardCharsets

class StandardCharsetsImportingCompilationCustomizer extends CompilationCustomizer {
    StandardCharsetsImportingCompilationCustomizer() {
        super(CONVERSION)
    }

    @Override
    void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
        source.AST.addStaticStarImport(StandardCharsets.simpleName, ClassHelper.make(StandardCharsets))
    }
}
