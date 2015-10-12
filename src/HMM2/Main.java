package HMM2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HMM hmm = new HMM();
		BaumWelch baumWelch = new BaumWelch();
		String path = "C:\\Users\\zhangzhizhi\\Documents\\Everyone\\张志智\\课程\\机器学习\\大作业\\letter.data\\Step3_train\\";
		try {
			List list = new ArrayList<String>();
			String s = null;
			//读写文件
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(path+"word_uniq.data")));
			//循环遍历
			while ((s = br.readLine()) != null) {
				
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int[][] O = {{1,2},{1,2}};
		baumWelch.coreByMutiObj(hmm, O, 1000);
	}

}
