package grafo.mdp.structure;

import grafo.optilib.structure.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MDPInstance implements Instance {

    private int n;
    private int m;
    private float[][] distances;
    private String name;

    public MDPInstance(String path) {
        readInstance(path);
    }

    @Override
    public void readInstance(String s) {
        name = s.substring(s.lastIndexOf('/')+1, s.lastIndexOf('.'));
        try (BufferedReader bf = new BufferedReader(new FileReader(s))) {
            String[] tokens = bf.readLine().split("\\s+");
            n = Integer.parseInt(tokens[0]);
            m = Integer.parseInt(tokens[1]);
            distances = new float[n][n];
            for (int i = 0; i < n * (n - 1) / 2; i++) {
                tokens = bf.readLine().split("\\s+");
                int u = Integer.parseInt(tokens[0]);
                int v = Integer.parseInt(tokens[1]);
                float d = Float.parseFloat(tokens[2]);
                distances[u][v] = d;
                distances[v][u] = d;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public float distance(int u, int v) {
        return distances[u][v];
    }

    public String getName() {
        return name;
    }
}
