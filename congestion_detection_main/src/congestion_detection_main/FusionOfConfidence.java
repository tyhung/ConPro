package congestion_detection_main;
import java.util.*;

public class FusionOfConfidence {

	private List<Double> dataOfFusion = new ArrayList<Double>();	// [與"判斷街道"的相鄰街道之集合] 會包含相鄰街道的集合, 每一個元素代表其相鄰街道的信心值
	
	// 設定基本資料 = [與"判斷街道"的相鄰街道之集合] 會包含相鄰街道的集合, 每一個元素代表其相鄰街道的信心值
	public FusionOfConfidence(List<Double> data) {
		for(int i = 0; i < data.size(); i++) 
            this.dataOfFusion.add(data.get(i)); 
	}
	
	// 驗證是否目前路段是否壅塞
	public double verifyOfConfidence() {		
		double counterOfCongestion = 0;		
		
		// 累計總共有幾個街道判斷為可能壅塞路段, 以判斷目前街道是否為壅塞路段
		for(int i = 0; i < this.dataOfFusion.size(); i++) 
            if(this.dataOfFusion.get(1) <= 3) counterOfCongestion++; // 當信心值小於3 則視為壅塞
		
		if((counterOfCongestion / this.dataOfFusion.size()) >= 0.5) counterOfCongestion = -1; // 當此集合有一半以上為壅塞, 則此路段可能為壅塞, 設定 1 = 壅塞
		return counterOfCongestion;
	}
		
	// 輸出此街道與相鄰街道的集合
	public void printValue(List<Double> data) {
		double sum = 0;
		for(int i = 0; i < data.size(); i++) { 
            System.out.print(this.dataOfFusion.get(i) + " ");
            sum += this.dataOfFusion.get(i);
		}
		System.out.print(sum);
	}
}
