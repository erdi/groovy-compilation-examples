import groovy.transform.InheritConstructors
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.transform.stc.AbstractTypeCheckingExtension
import org.slf4j.Logger

@InheritConstructors
class LogBindingTypeCheckingExtension extends AbstractTypeCheckingExtension {

    @Override
    boolean handleUnresolvedVariableExpression(VariableExpression vexp) {
        if (vexp.name == 'log') {
            storeType(vexp, ClassHelper.make(Logger))
            return true
        }
    }
}
