import java.util.LinkedList;

/**
 * List to store the Parameters and a method to retrieve them
 */
class ParameterList extends LinkedList<Parameter> {

    /**
     * returns the parameter with the given flagName
     *
     * @param flag flagName (long or short version) of the parameter
     * @return parameter with the given flagName
     */
    public Parameter getParameterFromList(String flag) {
        for (Parameter p : this) {
            if (p.getFlagName().equals(flag) || (p.getShortName() != null && p.getShortName().equals(flag))) {
                return p;
            }
        }
        return null;
    }
}
