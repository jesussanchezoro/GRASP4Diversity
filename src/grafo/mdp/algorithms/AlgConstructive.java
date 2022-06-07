package grafo.mdp.algorithms;

import grafo.mdp.structure.MDPInstance;
import grafo.mdp.structure.MDPSolution;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.results.Result;
import grafo.optilib.tools.Timer;

public class AlgConstructive implements Algorithm<MDPInstance, MDPSolution> {

    private Constructive<MDPInstance, MDPSolution> c;
    private int nConstructions;
    private MDPSolution best;

    public AlgConstructive(Constructive<MDPInstance, MDPSolution> c, int nConstructions) {
        this.c = c;
        this.nConstructions = nConstructions;
    }

    @Override
    public Result execute(MDPInstance instance) {
        best = null;
        System.out.print(instance.getName()+"\t");
        Timer.initTimer();
        for (int i = 0; i < nConstructions; i++) {
            MDPSolution sol = c.constructSolution(instance);
            if (best == null || Float.compare(sol.getDiversity(), best.getDiversity()) > 0) {
                best = sol;
            }
        }
        double secs = Timer.getTime() / 1000.0;
        System.out.println(best.getDiversity()+"\t"+secs);
        Result r = new Result(instance.getName());
        r.add("Diversity", best.getDiversity());
        r.add("Time (s)", secs);
        return r;
    }

    @Override
    public MDPSolution getBestSolution() {
        return best;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"("+c+","+nConstructions+")";
    }
}
