package congestion_detection_main;
import java.util.*;

public class FusionOfConfidence {

	private List<Double> dataOfFusion = new ArrayList<Double>();	// [�P"�P�_��D"���۾F��D�����X] �|�]�t�۾F��D�����X, �C�@�Ӥ����N���۾F��D���H�߭�
	
	// �]�w�򥻸�� = [�P"�P�_��D"���۾F��D�����X] �|�]�t�۾F��D�����X, �C�@�Ӥ����N���۾F��D���H�߭�
	public FusionOfConfidence(List<Double> data) {
		for(int i = 0; i < data.size(); i++) 
            this.dataOfFusion.add(data.get(i)); 
	}
	
	// ���ҬO�_�ثe���q�O�_�ö�
	public double verifyOfConfidence() {		
		double counterOfCongestion = 0;		
		
		// �֭p�`�@���X�ӵ�D�P�_���i��ö���q, �H�P�_�ثe��D�O�_���ö���q
		for(int i = 0; i < this.dataOfFusion.size(); i++) 
            if(this.dataOfFusion.get(1) <= 3) counterOfCongestion++; // ��H�߭Ȥp��3 �h�����ö�
		
		if((counterOfCongestion / this.dataOfFusion.size()) >= 0.5) counterOfCongestion = -1; // �����X���@�b�H�W���ö�, �h�����q�i�ର�ö�, �]�w 1 = �ö�
		return counterOfCongestion;
	}
		
	// ��X����D�P�۾F��D�����X
	public void printValue(List<Double> data) {
		double sum = 0;
		for(int i = 0; i < data.size(); i++) { 
            System.out.print(this.dataOfFusion.get(i) + " ");
            sum += this.dataOfFusion.get(i);
		}
		System.out.print(sum);
	}
}
