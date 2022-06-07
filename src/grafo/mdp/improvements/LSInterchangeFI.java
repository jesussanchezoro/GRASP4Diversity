package grafo.mdp.improvements;

import grafo.mdp.structure.MDPInstance;
import grafo.mdp.structure.MDPSolution;
import grafo.optilib.metaheuristics.Improvement;

import java.util.ArrayList;
import java.util.List;

public class LSInterchangeFI implements Improvement<MDPSolution> {


    @Override
    public void improve(MDPSolution sol) {
        boolean improve = true;
        while (improve) {
            improve = tryImprove(sol);
        }
    }

    private int compareFloat(float f1, float f2) {
        if (Math.abs(f1-f2) < 0.01) {
            return 0;
        } else if (f1 < f2) {
            return -1;
        } else {
            return +1;
        }
    }

    private boolean tryImprove(MDPSolution sol) {
        MDPInstance instance = sol.getInstance();
        int n = sol.getInstance().getN();
        List<Integer> inSol = new ArrayList<>(n);
        List<Integer> outSol = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (sol.contains(i)) {
                inSol.add(i);
            } else {
                outSol.add(i);
            }
        }
        boolean improve = false;
        for (int s : inSol) {
            float contribS = sol.contributionOfElement(s);
            for (int o : outSol) {
                float contribO = sol.contributionOfElement(o) - instance.distance(o,s);
                if (compareFloat(contribO, contribS) > 0) {
                    sol.remove(s, contribS);
                    sol.add(o, contribO);
                    return true;
                }
            }
        }
        return improve;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
