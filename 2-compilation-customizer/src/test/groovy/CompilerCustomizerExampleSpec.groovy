import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.CompilationCustomizer
import spock.lang.Specification

class CompilerCustomizerExampleSpec extends Specification {

    def 'run fails if StandardCharsets are not imported'() {
        when:
        runScript('''
            UTF_8.name()
        ''')

        then:
        def e = thrown(MissingPropertyException)
        e.message.startsWith("No such property: UTF_8")
    }

    def 'run succeeds if StandardCharsets are imported via a compilation customizer'() {
        when:
        def result = runScript([new StandardCharsetsImportingCompilationCustomizer()], '''
            UTF_8.name()
        ''')

        then:
        result == "UTF-8"
    }

    private Object runScript(List<CompilationCustomizer> customizers = [], String script) {
        def shell = new GroovyShell(compilerConfiguration(customizers))
        shell.evaluate(script)
    }

    private CompilerConfiguration compilerConfiguration(List<CompilationCustomizer> customizers) {
        def config = new CompilerConfiguration()
        config.addCompilationCustomizers(*customizers)
        config
    }
}
