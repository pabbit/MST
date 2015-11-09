package org.pabbit.MST;

import edu.emory.mathcs.cs323.graph.span.*;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import edu.emory.mathcs.cs323.graph.Edge;
import edu.emory.mathcs.cs323.graph.Graph;

public class MSTKang 
{
	private List<String>  words;
	private List<float[]> vectors;
	private float[][] distance;
	
	public void readVectors(InputStream in) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Pattern p = Pattern.compile("\t");
		String line, word;
		float[] vector;
		String[] t;
		
		words   = new ArrayList<>();
		vectors = new ArrayList<>();
		
		while ((line = reader.readLine()) != null)
		{
			t = p.split(line);
			word = t[0];
			vector = new float[t.length-1];
			
			for (int i=1; i<t.length; i++)
				vector[i-1] = Float.parseFloat(t[i]);
			
			words.add(word);
			vectors.add(vector);
		}
		
		distance = new float[words.size()][words.size()];

		for(int i = 1; i < distance.length; i++)
			for(int j = 0; j < i; j++)
			{
				distance[i][j] = getEuclideanDistance(vectors.get(i), vectors.get(j));
				distance[j][i] = distance[i][j];
			}
		
		reader.close();
	}
	
	public SpanningTree findMinimumSpanningTree()
	{
		SpanningTree tree = new SpanningTree();
		
		// TODO: This code produces a dummy graph
		Random rand = new Random();
		int size = words.size();
		
		for (int source=0; source<size; source++)
		{
			int target = 0;
			
			while (true)
			{
				target = rand.nextInt(size);
				if (target != source) break;
			}
			
			tree.addEdge(new Edge(source, target, rand.nextFloat()));
		}
		
		return tree;
	}
	
	public float getEuclideanDistance(float[] v1, float[] v2)
	{		
		float total = 0;
		
		for(int i = 0; i < v1.length; i++)
		{
			total += (float) Math.pow(v1[i]-v2[i], 2);
		}
		
		total = (float) Math.sqrt(total);
		return total;
	}
	
	public float getCosineDistance(float[] v1, float[] v2)
	{
		// TODO:
		return 0;
	}
	
	public void printSpanningTree(OutputStream out, SpanningTree tree)
	{
		PrintStream fout = new PrintStream(new BufferedOutputStream(out));
		fout.println("digraph G {");
		
		for (Edge edge : tree.getEdges())
			fout.printf("\"%s\" -> \"%s\"[label=\"%5.4f\"];\n", words.get(edge.getSource()), words.get(edge.getTarget()), edge.getWeight());
		
		fout.println("}");
		fout.close();
	}
	
	static public void main(String[] args) throws Exception
	{
		final String INPUT_FILE  = "./src/graph/span/word_vectors.txt";
		final String OUTPUT_FILE = "./src/graph/span/word_vectors.dot";
		
		MSTKang mst = new MSTKang();
		
		mst.readVectors(new FileInputStream(INPUT_FILE));
		SpanningTree tree = mst.findMinimumSpanningTree();
		mst.printSpanningTree(new FileOutputStream(OUTPUT_FILE), tree);
		System.out.println(tree.getTotalWeight());
	}
}
