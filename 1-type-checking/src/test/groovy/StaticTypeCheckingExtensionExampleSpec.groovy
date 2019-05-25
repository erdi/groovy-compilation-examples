import groovy.transform.TypeChecked
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.MultipleCompilationErrorsException
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.transform.stc.TypeCheckingExtension
import org.slf4j.Logger
import spock.lang.Specification

class StaticTypeCheckingExtensionExampleSpec extends Specification {

    def log = Mock(Logger)

    def 'run fails when compiling with static type checking'() {
        when:
        runScriptWithTypeChecking(log: log, '''
            log.info('Hello GR8Conf!')
        ''')

        then:
        def e = thrown(MultipleCompilationErrorsException)
        def errorMessage = e.errorCollector.errors.first() as SyntaxErrorMessage
        errorMessage.cause.message.startsWith('[Static type checking] - The variable [log] is undeclared')
    }

    def 'run succeeds when type checking is made aware of log variable using an extension'() {
        when:
        runScriptWithTypeChecking([LogBindingTypeCheckingExtension], log: log, '''
            log.info('Hello GR8Conf!')
        ''')

        then:
        1 * log.info('Hello GR8Conf!')
    }

    private Object runScriptWithTypeChecking(Map<String, ?> bindings,
                                             List<Class<? extends TypeCheckingExtension>> extensions = [], String script) {
        def shell = new GroovyShell(typeCheckingCompilerConfiguration(extensions))
        bindings.each { name, value ->
            shell.setVariable(name, value)
        }
        shell.evaluate(script)
    }

    private CompilerConfiguration typeCheckingCompilerConfiguration(
        List<Class<? extends TypeCheckingExtension>> extensions
    ) {
        def config = new CompilerConfiguration()
        config.addCompilationCustomizers(
            new ASTTransformationCustomizer(
                TypeChecked,
                extensions: extensions*.name
            )
        )
        config
    }
}
