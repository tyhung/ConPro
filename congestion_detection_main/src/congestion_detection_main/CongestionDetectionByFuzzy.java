package congestion_detection_main;

public class CongestionDetectionByFuzzy {
	public static double controlRate = 0.5;	// �վ���v�H�߭Ȫ��ǲ߲v
	public static double[] rateOfrangeOfLimit = {0.2, 0.3, 0.6}; // [0] = �ö��I, [1] = �ö�P�κZ�ҽk����I, [2] = �κZ�I
	
	private double limitOfVelocity;	// �ثe���q��"�t��"
	private double currentVelocity;	// �ثe���q��"�t��"
	private double currentOfConfidence = -1;	// �w�]�ثe��"�H�߭�"
	private double historyOfConfidence;	// �P�_�O�_���ĳ]��"���v�H�߭�"
	private double tempData = 0;	// �Ȧs�H�߭ȸ��
	
	private double[] valueOfRangeOfLimit = new double[3];	// [0] = �ö�t��, [1] = �ö�P�κZ�ҽk��ɳt��, [2] = �κZ�t��
	
	// �w�]�Ѽ�, �åB���C��l�H�߭�
	public CongestionDetectionByFuzzy() {
		this(100.0, 100.0, 1.0);
	}
	
	// �]�w�򥻸��, (�ثe���q��"�t��", �ثe���q��"�t��", �ثe���q��"���v�H�߭�")
	public CongestionDetectionByFuzzy(double velLim, double vel, double conf) {
		this.limitOfVelocity = velLim;
		this.currentVelocity = vel;
		this.historyOfConfidence = conf;
	}
	
	public double updateConfidence() {
		this.valueOfRangeOfLimit[0] = limitOfVelocity * rateOfrangeOfLimit[0];	// [0] = �ö�t��,
		this.valueOfRangeOfLimit[1] = limitOfVelocity * rateOfrangeOfLimit[1];	// [1] = �ö�P�κZ�ҽk��ɳt��
		this.valueOfRangeOfLimit[2] = limitOfVelocity * rateOfrangeOfLimit[2];	// [2] = �κZ�t��
		
		// �p��ثe���H�߭�, �̾�Fuzzy
		if(this.currentVelocity <= this.valueOfRangeOfLimit[0]) 
			this.currentOfConfidence = 1;
		else if(this.currentVelocity > this.valueOfRangeOfLimit[0] && this.currentVelocity < this.valueOfRangeOfLimit[1])
			this.currentOfConfidence = 1 - ((this.currentOfConfidence - this.valueOfRangeOfLimit[0]) / (this.valueOfRangeOfLimit[1] - this.valueOfRangeOfLimit[0]));
		else if(this.currentVelocity >= this.valueOfRangeOfLimit[1] && this.currentVelocity < this.valueOfRangeOfLimit[2])
			this.currentOfConfidence = 0 - ((this.currentOfConfidence - this.valueOfRangeOfLimit[1]) / (this.valueOfRangeOfLimit[2] - this.valueOfRangeOfLimit[1]));
		else 
			this.currentOfConfidence = -1;
		
		// ��s"���v�H�߭�"
		this.tempData = controlRate * historyOfConfidence + (1 - controlRate) * currentOfConfidence;
		if(this.tempData < 0)
			this.historyOfConfidence = 0;
		else if (this.tempData <=5)
			this.historyOfConfidence = this.tempData;
		
		//�^��"���v�H�߭�"
		return this.historyOfConfidence;
	}
}
