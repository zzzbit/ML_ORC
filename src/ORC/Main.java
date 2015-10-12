package ORC;

import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * 主入口程序
 * 
 * @author zhangzhizhi
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// HMM项所占的比重，重要参数
		double factor = 0.1;
		// HMM训练得到的模型
		double[] HMM_model = { 0.0776255707762557, 0.0228310502283105,
				0.0410958904109589, 0.0273972602739726, 0.0958904109589042,
				0.0182648401826484, 0.0433789954337900, 0.0159817351598174,
				0.0936073059360730, 0.00456621004566210, 0.0159817351598174,
				0.0616438356164384, 0.0319634703196347, 0.0958904109589042,
				0.0730593607305936, 0.0251141552511415, 0.00684931506849315,
				0.0502283105022831, 0.0273972602739726, 0.0479452054794520,
				0.0479452054794520, 0.0136986301369863, 0.00913242009132420,
				0.00684931506849315, 0.0251141552511415, 0.0205479452054795 };
		try {
			/**
			 * 1.声明分类器
			 */
			Classifier[] classifier = new Classifier[1];
			double[] result = new double[classifier.length];
			/**
			 * 2.读入训练、测试样本
			 */
			String path = "C:\\Users\\zhangzhizhi\\Documents\\Everyone\\张志智\\课程\\机器学习\\大作业\\letter.data\\";
			File inputFile = new File(path + "Step3_train\\hog.arff");// 训练语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet(); // 读入训练文件

			inputFile = new File(path + "Step3_test\\hog.arff");// 测试语料文件
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // 读入测试文件

			/**
			 * 3.设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数
			 */
			instancesTest.setClassIndex(0);
			instancesTrain.setClassIndex(0);

			/**
			 * 4.初始化分类器类型
			 */
			//weka.classifiers.functions.LibSVM
			//weka.classifiers.bayes.NaiveBayes
			//weka.classifiers.trees.J48
			//weka.classifiers.rules.ZeroR
			
			// LibSVM
			classifier[0] = (Classifier) Class.forName(
					"weka.classifiers.lazy.IBk").newInstance();

			/**
			 * 5.训练分类器
			 */
			for (int i = 0; i < classifier.length; i++){
				classifier[i].buildClassifier(instancesTrain);
			}
			
			/**
			 * 6.整合分类结果及准确率
			 */
			double[] distribution;
			Evaluation eval = new Evaluation(instancesTrain);
			for (int i = 0; i < classifier.length; i++){
				int right = 0;	//该分类器正确数
				int wrong = 0;	//该分类器错误数
				for (int j = 0; j < instancesTest.numInstances(); j++){
					distribution = classifier[i].distributionForInstance(instancesTest.instance(j));
					for (int k = 0; k < distribution.length; k++){
						distribution[k] += factor*HMM_model[k];
					}
					double max = 0;
					int index = 0;
					
					for (int k = 0; k < distribution.length; k++){
						if (distribution[k] > max){
							max = distribution[k];
							index = k;
						}
					}
					
					int rightValue = (int)instancesTest.instance(j).classValue();
					if (index == rightValue){
						right++;
					}
					else {
						wrong++;
					}
				}
				//输出结果
				result[i] = right*1.0/(right+wrong);
				//单独分类器结果
				eval.evaluateModel(classifier[i], instancesTest);
				System.out.println("单独分类器准确率："+(1-eval.errorRate()));
				System.out.println("加入HMM准确率："+result[i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
