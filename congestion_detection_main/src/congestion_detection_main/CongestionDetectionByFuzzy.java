package congestion_detection_main;

public class CongestionDetectionByFuzzy {
	public static double controlRate = 0.5;	// 調整歷史信心值的學習率
	public static double[] rateOfrangeOfLimit = {0.2, 0.3, 0.6}; // [0] = 壅塞點, [1] = 壅塞與舒暢模糊交界點, [2] = 舒暢點
	
	private double limitOfVelocity;	// 目前路段的"速限"
	private double currentVelocity;	// 目前路段的"速度"
	private double currentOfConfidence = -1;	// 預設目前的"信心值"
	private double historyOfConfidence;	// 判斷是否為傭設的"歷史信心值"
	private double tempData = 0;	// 暫存信心值資料
	
	private double[] valueOfRangeOfLimit = new double[3];	// [0] = 壅塞速度, [1] = 壅塞與舒暢模糊交界速度, [2] = 舒暢速度
	
	// 預設參數, 並且降低原始信心值
	public CongestionDetectionByFuzzy() {
		this(100.0, 100.0, 1.0);
	}
	
	// 設定基本資料, (目前路段的"速限", 目前路段的"速度", 目前路段的"歷史信心值")
	public CongestionDetectionByFuzzy(double velLim, double vel, double conf) {
		this.limitOfVelocity = velLim;
		this.currentVelocity = vel;
		this.historyOfConfidence = conf;
	}
	
	public double updateConfidence() {
		this.valueOfRangeOfLimit[0] = limitOfVelocity * rateOfrangeOfLimit[0];	// [0] = 壅塞速度,
		this.valueOfRangeOfLimit[1] = limitOfVelocity * rateOfrangeOfLimit[1];	// [1] = 壅塞與舒暢模糊交界速度
		this.valueOfRangeOfLimit[2] = limitOfVelocity * rateOfrangeOfLimit[2];	// [2] = 舒暢速度
		
		// 計算目前的信心值, 依據Fuzzy
		if(this.currentVelocity <= this.valueOfRangeOfLimit[0]) 
			this.currentOfConfidence = 1;
		else if(this.currentVelocity > this.valueOfRangeOfLimit[0] && this.currentVelocity < this.valueOfRangeOfLimit[1])
			this.currentOfConfidence = 1 - ((this.currentOfConfidence - this.valueOfRangeOfLimit[0]) / (this.valueOfRangeOfLimit[1] - this.valueOfRangeOfLimit[0]));
		else if(this.currentVelocity >= this.valueOfRangeOfLimit[1] && this.currentVelocity < this.valueOfRangeOfLimit[2])
			this.currentOfConfidence = 0 - ((this.currentOfConfidence - this.valueOfRangeOfLimit[1]) / (this.valueOfRangeOfLimit[2] - this.valueOfRangeOfLimit[1]));
		else 
			this.currentOfConfidence = -1;
		
		// 更新"歷史信心值"
		this.tempData = controlRate * historyOfConfidence + (1 - controlRate) * currentOfConfidence;
		if(this.tempData < 0)
			this.historyOfConfidence = 0;
		else if (this.tempData <=5)
			this.historyOfConfidence = this.tempData;
		
		//回傳"歷史信心值"
		return this.historyOfConfidence;
	}
}
