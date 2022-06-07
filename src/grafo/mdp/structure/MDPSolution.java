package grafo.mdp.structure;

import grafo.optilib.structure.Solution;

import java.util.HashSet;
import java.util.Set;

public class MDPSolution implements Solution {

    private MDPInstance instance;
    private Set<Integer> solution;
    private float diversity;

    public MDPSolution(MDPInstance instance) {
        this.instance = instance;
        this.solution = new HashSet<>(instance.getM());
    }

    public void copy(MDPSolution sol) {
        this.instance = sol.instance;
        this.solution = new HashSet<>(sol.solution);
        this.diversity = sol.diversity;
    }

    public void add(int v) {
        diversity += contributionOfElement(v);
        solution.add(v);
    }

    public void add(int v, float contrib) {
        solution.add(v);
        diversity += contrib;
    }

    public void remove(int v) {
        solution.remove(v);
        diversity -= contributionOfElement(v);
    }

    public void remove(int v, float contrib) {
        solution.remove(v);
        diversity -= contrib;
    }

    public float contributionOfElement(int v) {
        float contrib = 0;
        for (int s : solution) {
            contrib += instance.distance(v,s);
        }
        return contrib;
    }

    public Set<Integer> getSolution() {
        return solution;
    }

    public boolean contains(int v) {
        return solution.contains(v);
    }

    public float getDiversity() {
        return diversity;
    }

    public boolean isFeasible() {
        return solution.size() == instance.getM();
    }

    public MDPInstance getInstance() {
        return instance;
    }

    public int size() {
        return solution.size();
    }
}
