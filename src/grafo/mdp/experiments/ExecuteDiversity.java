package grafo.mdp.experiments;

import grafo.mdp.algorithms.AlgConstructive;
import grafo.mdp.algorithms.AlgConstructiveLS;
import grafo.mdp.algorithms.ParallelAlgConstructiveLS;
import grafo.mdp.constructives.CGR;
import grafo.mdp.improvements.LSInterchangeBI;
import grafo.mdp.improvements.LSInterchangeFI;
import grafo.mdp.structure.MDPInstance;
import grafo.mdp.structure.MDPInstanceFactory;
import grafo.mdp.structure.MDPSolution;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.results.Experiment;

public class ExecuteDiversity {

    public static void main(String[] args) {
        // Algorithms to be executed
        Algorithm<MDPInstance, MDPSolution>[] algs = new Algorithm[] {
//                new AlgConstructive(new CGR(0.25f), 100),
//                new AlgConstructive(new CGR(0.50f), 100),
//                new AlgConstructive(new CGR(0.75f), 100),
//                new AlgConstructive(new CGR(-1), 100),
//                new ParallelAlgConstructiveLS(new CGR(0.25f), new LSInterchangeFI(), 100),
                new ParallelAlgConstructiveLS(new CGR(0.50f), new LSInterchangeFI(), 100),
//                new ParallelAlgConstructiveLS(new CGR(0.75f), new LSInterchangeFI(), 100),
//                new ParallelAlgConstructiveLS(new CGR(-1), new LSInterchangeFI(), 100),
//                new ParallelAlgConstructiveLS(new CGR(0.25f), new LSInterchangeBI(), 100),
//                new ParallelAlgConstructiveLS(new CGR(0.50f), new LSInterchangeBI(), 100),
//                new ParallelAlgConstructiveLS(new CGR(0.75f), new LSInterchangeBI(), 100),
                new ParallelAlgConstructiveLS(new CGR(-1), new LSInterchangeBI(), 100),
        };

        String instanceSet = "preliminar";
        String path = "/Volumes/Jesus/mdplib_2/cap_dispersion/";
        if (args.length > 0) {
            instanceSet = args[1];
            path = args[0];
        }
        path += instanceSet;

        MDPInstanceFactory factory = new MDPInstanceFactory();
        Experiment<MDPInstance, MDPInstanceFactory, MDPSolution> experiment = new Experiment<>(algs, factory);
        experiment.launch(path);
    }
}
