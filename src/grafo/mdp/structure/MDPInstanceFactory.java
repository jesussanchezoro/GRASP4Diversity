package grafo.mdp.structure;

import grafo.optilib.structure.Instance;
import grafo.optilib.structure.InstanceFactory;

public class MDPInstanceFactory extends InstanceFactory<MDPInstance> {

    @Override
    public MDPInstance readInstance(String s) {
        return new MDPInstance(s);
    }
}
