package grafo.mdp.algorithms;

import grafo.mdp.structure.MDPInstance;
import grafo.mdp.structure.MDPSolution;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.metaheuristics.Improvement;
import grafo.optilib.results.Result;
import grafo.optilib.tools.Timer;

import java.util.PriorityQueue;

public class AlgConstructiveLS implements Algorithm<MDPInstance, MDPSolution> {

    private Constructive<MDPInstance, MDPSolution> c;
    private Improvement<MDPSolution> ls;
    private int nConstructions;
    private MDPSolution best;

    public AlgConstructiveLS(Constructive<MDPInstance, MDPSolution> c, Improvement<MDPSolution> ls, int nConstructions) {
        this.c = c;
        this.ls = ls;
        this.nConstructions = nConstructions;
    }

    @Override
    public Result execute(MDPInstance instance) {
        best = null;
        System.out.print(instance.getName()+"\t");
        PriorityQueue<MDPSolution> sols = new PriorityQueue<>((s1, s2) -> -Float.compare(s1.getDiversity(), s2.getDiversity()));
        Timer.initTimer(100 * 1000);
        for (int i = 0; i < nConstructions; i++) {
//            System.out.print("\t"+i+"\t");
            MDPSolution sol = c.constructSolution(instance);
            sols.add(sol);
            if (best == null || Float.compare(sol.getDiversity(), best.getDiversity()) > 0) {
                best = sol;
            }
//            System.out.println("\t"+sol.getDiversity()+"\t"+best.getDiversity());
        }
        int improves = 0;
        while (!sols.isEmpty() && !Timer.timeReached()) {
            MDPSolution sol = sols.poll();
            ls.improve(sol);
            improves++;
        }
        double secs = Timer.getTime() / 1000.0;
        System.out.println(best.getDiversity()+"\t"+secs+"\t"+improves);
        Result r = new Result(instance.getName());
        r.add("Diversity", best.getDiversity());
        r.add("Time (s)", secs);
        r.add("Improves", improves);
        return r;
    }

    @Override
    public MDPSolution getBestSolution() {
        return best;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"("+c+","+ls+","+nConstructions+")";
    }
}
