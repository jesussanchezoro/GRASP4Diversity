package grafo.mdp.algorithms;

import grafo.mdp.structure.MDPInstance;
import grafo.mdp.structure.MDPSolution;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.metaheuristics.Improvement;
import grafo.optilib.results.Result;
import grafo.optilib.tools.Timer;

import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelAlgConstructiveLS implements Algorithm<MDPInstance, MDPSolution> {

    private Constructive<MDPInstance, MDPSolution> c;
    private Improvement<MDPSolution> ls;
    private int nConstructions;
    private MDPSolution best;

    public ParallelAlgConstructiveLS(Constructive<MDPInstance, MDPSolution> c, Improvement<MDPSolution> ls, int nConstructions) {
        this.c = c;
        this.ls = ls;
        this.nConstructions = nConstructions;
    }

    @Override
    public Result execute(MDPInstance instance) {
        ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(nConstructions);
        best = new MDPSolution(instance);
        System.out.print(instance.getName()+"\t");
        Timer.initTimer(100 * 1000);
        for (int i = 0; i < nConstructions; i++) {
            pool.submit(() -> {
                MDPSolution sol = c.constructSolution(instance);
                ls.improve(sol);
                synchronized (best) {
                    if (Float.compare(sol.getDiversity(), best.getDiversity()) > 0) {
                        best.copy(sol);
                    }
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        return this.getClass().getSimpleName()+"("+c+","+ls+","+nConstructions+")";
    }
}
