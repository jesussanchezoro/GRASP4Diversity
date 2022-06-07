package grafo.mdp.improvements;

import grafo.mdp.structure.MDPInstance;
import grafo.mdp.structure.MDPSolution;
import grafo.optilib.metaheuristics.Improvement;

import java.util.ArrayList;
import java.util.List;

public class LSInterchangeBI implements Improvement<MDPSolution> {


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
        int bestIn = -1;
        int bestOut = -1;
        float removeContrib = -1;
        float addContrib = -1;
        float bestOF = sol.getDiversity();
        for (int s : inSol) {
            float contribS = sol.contributionOfElement(s);
            for (int o : outSol) {
                float contribO = sol.contributionOfElement(o) - instance.distance(o,s);
                if (compareFloat(contribO, contribS) > 0) {
                    float of = sol.getDiversity() - contribS + contribO;
                    if (compareFloat(of, bestOF) > 0) {
                        bestOF = of;
                        removeContrib = contribS;
                        addContrib = contribO;
                        bestIn = o;
                        bestOut = s;
                        improve = true;
                    }
                }
            }
        }
        if (improve) {
            sol.remove(bestOut, removeContrib);
            sol.add(bestIn, addContrib);
        }
        return improve;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
