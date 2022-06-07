package grafo.mdp.constructives;

import grafo.mdp.structure.MDPInstance;
import grafo.mdp.structure.MDPInstanceFactory;
import grafo.mdp.structure.MDPSolution;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CGR implements Constructive<MDPInstance, MDPSolution> {

    public class Candidate {
        int c;
        float contribution;

        public Candidate(int c, float contribution) {
            this.c = c;
            this.contribution = contribution;
        }
    }

    public class GMinMax {
        float gmin;
        float gmax;

        public GMinMax(float gmin, float gmax) {
            this.gmin = gmin;
            this.gmax = gmax;
        }
    }

    private float alpha;

    public CGR(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public MDPSolution constructSolution(MDPInstance instance) {
        GMinMax gminmax = new GMinMax(0,0);
        MDPSolution sol = new MDPSolution(instance);
        int first = RandomManager.getRandom().nextInt(instance.getN());
        sol.add(first);
        float realAlpha = alpha < 0 ? RandomManager.getRandom().nextFloat() : alpha;
        int[] rcl = new int[instance.getN()];
        int limit = 0;
        List<Candidate> cl = createCL(sol, first, gminmax);
        while (!sol.isFeasible()) {
            float threshold = gminmax.gmax - realAlpha * (gminmax.gmax - gminmax.gmin);
            limit = 0;
            for (int i = 0; i < cl.size(); i++) {
                Candidate c = cl.get(i);
                if (Float.compare(c.contribution, threshold) >= 0) {
                    rcl[limit] = i;
                    limit++;
                }
            }
            int idSel = rcl[RandomManager.getRandom().nextInt(limit)];
            Candidate next = cl.remove(idSel);
            sol.add(next.c, next.contribution);
            updateCL(sol, cl, next.c, gminmax);
        }
        return sol;
    }

    private List<Candidate> createCL(MDPSolution sol, int first, GMinMax gminmax) {
        int n = sol.getInstance().getN();
        List<Candidate> cl = new ArrayList<>(n);
        gminmax.gmin = 0x3f3f3f;
        gminmax.gmax = 0;
        for (int c = 0; c < n; c++) {
            if (c != first) {
                float contrib = sol.contributionOfElement(c);
                gminmax.gmin = Math.min(gminmax.gmin, contrib);
                gminmax.gmax = Math.max(gminmax.gmax, contrib);
                cl.add(new Candidate(c, contrib));
            }
        }
        return cl;
    }

    private void updateCL(MDPSolution sol, List<Candidate> cl, int added, GMinMax gminmax) {
        MDPInstance instance = sol.getInstance();
        gminmax.gmin = 0x3f3f3f;
        gminmax.gmax = 0;
        for (Candidate c : cl) {
            c.contribution += instance.distance(c.c, added);
            gminmax.gmin = Math.min(c.contribution, gminmax.gmin);
            gminmax.gmax = Math.max(c.contribution, gminmax.gmax);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"("+alpha+")";
    }
}
