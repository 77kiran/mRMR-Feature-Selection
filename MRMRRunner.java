
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import weka.core.Instance;
import weka.core.Instances;


public class MRMRRunner {

	
	/*
	 * Takes input as
	 * candidateSet is the list with index of features in the data. 
	 *  and noOfFeaturesReqd is the number of features required in the end.
	 *  Returns the list containing index of selected features.
	 */
	
	public List<Integer> getSelectedFeatures(List<Integer> candidateSet ,int noOfFeaturesReqd){
		Set<Integer> selectedFeatureSet=new HashSet<Integer>();
		double relevance;
		double redundancy;
		double mrmrValue[]=new double[FeatureSelection.noOfFeatures];
		int features[]=new int[FeatureSelection.noOfFeatures];
		for(int i=0;i<features.length;i++){
			features[i]=-1;
		}
		for(int feature:candidateSet){
			features[feature]=feature;
			relevance=FeatureSelection.Feat_ClassSU.get(feature);
			redundancy=0;
			for(int otherFeature:candidateSet){
				if(feature!=otherFeature){
					redundancy=redundancy + MutualInformation.calculateMutualInformation(FeatureSelection.occurence[feature], FeatureSelection.occurence[otherFeature]);
				}
			}
			double tempValue=relevance - redundancy;
			String doubleVal=new DecimalFormat("#0.000000").format(tempValue);
			mrmrValue[feature]=Double.parseDouble(doubleVal);
		}
		
		int sortedFeatures[]=QuickSort.quicksort(mrmrValue, features);
		List<Integer> selectedFeatList=new ArrayList<Integer>(selectedFeatureSet);
		for(int i=sortedFeatures.length -1;i >=0;i--){
			if(selectedFeatList.size() < noOfFeaturesReqd){
				int featureSelected= features[i];
				if(featureSelected!=-1){
					selectedFeatList.add(featureSelected);
				}
			}else{
				break;
			}
		}
		
		
		return selectedFeatList;
	}
	
	
}
