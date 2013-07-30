package congestion_detection_main;

import java.util.*;

public class CongestionDetection {
	public static void main(String[] args) {
	
		
		//CongestionDetectionByFuzzy cd = new CongestionDetectionByFuzzy(100,20,2);
		//System.out.println("output: " + cd.updateConfidence()); 
		
		List<Double> list = new ArrayList<Double>();
		for(int i = 0; i < 10; i++) 
            list.add((double)i); 
		
		FusionOfConfidence ff = new FusionOfConfidence(list);
		ff.printValue(list);
	}
}
