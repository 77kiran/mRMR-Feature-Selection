

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
*  This class reads data from .arff file and calls MRMRRunner.java.
*	finalList contains the list of indexes of features selected using mRMR algorithm.
*/


import weka.core.Instance;
import weka.core.Instances;

public class FeatureSelection {
	
	static int noOfFeatures;
	static int noOfInstances;
	static int noOfClass;
	static HashMap<Integer, Double> Feat_ClassSU=new HashMap<>();
	static ArrayList<Integer> relevantFeatList=new ArrayList<>();
	static HashMap<Integer,HashMap> SUList=new HashMap<>();
	static HashMap<Integer,Integer> vertexToFeature=new HashMap<>();
	static double occurence[][];
	@SuppressWarnings("unchecked")
	public static void  main() {
		
		String path= // path to .arff data file;
		BufferedReader buffer=new BufferedReader(new FileReader(path));
		Instances data=new Instances(buffer);
		
		int featuresRequired=100;
		List<Integer> finalList=new ArrayList<>();
		relevantFeatList=new ArrayList<>();
		SUList=new HashMap<>();
		vertexToFeature=new HashMap<>();
		Feat_ClassSU=new HashMap<>();
		noOfFeatures=data.numAttributes();
		noOfInstances=data.numInstances();
		noOfClass=data.numClasses();
		data.setClassIndex(noOfFeatures-1);
		occurence=new double[noOfFeatures][noOfInstances];
		//double relevanceVector[]=new double[attributes-1];
		
		int row=0;
		while(row < noOfInstances){
			Instance inst=data.instance(row); 
			for(int i=0;i<inst.numAttributes();i++){
				occurence[i][row]=inst.value(i);
			}
			row++;
		}
		
		getFeatClassMI(occurence);
		LinkedHashMap<Integer,Double> sortedFC=(LinkedHashMap<Integer, Double>) sortByValue(Feat_ClassSU);
		
		
		List<Integer> candidateFeatureList=new ArrayList<>();
		candidateFeatureList.addAll(sortedFC.keySet());
		finalList=new MRMRRunner().getSelectedFeatures(candidateFeatureList,featuresRequired);
		
	}
	
	
	public static void getFeatClassMI(double occurence[][]){
		double mutualInfo=0;
		for(int j=0;j<(noOfFeatures-1);j++){
			mutualInfo=MutualInformation.calculateMutualInformation(occurence[noOfFeatures-1], occurence[j]);
			Feat_ClassSU.put(j,mutualInfo);
				relevantFeatList.add(j);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map sortByValue(Map unsortMap) {	 
		List list = new LinkedList(unsortMap.entrySet());
	 
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
							.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
	 
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
