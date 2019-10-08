package com.ehr;

import org.apache.log4j.Logger;

public class Test {
	Logger LOG = Logger.getLogger(Test.class);
	
	public int solution(Tree t){
		
		int leftHeight = 0; 
		int rightHeight = 0;
		int treeDepth = 0;
		
		if(null!=t.l) {
			leftHeight++;
			LOG.debug("leftHeight:"+leftHeight);
		} 
		
		if(null!=t.r) {
			rightHeight++;
			LOG.debug("rightHeight:"+rightHeight);
		}
		
		treeDepth = Math.max(leftHeight, rightHeight);
		
		return treeDepth;
	}
	
	public Boolean hasNode(Tree t) {
		return null;
	}
	
	public static void main(String[] args) {
		
	      Tree T = new Tree();
	      Tree T1 = new Tree();
	      Tree T2 = new Tree();
	      Tree T3 = new Tree();
	      Tree T4 = new Tree();
	      Tree T5 = new Tree();
	      Tree T6 = new Tree();
	      T.x = 1;
	      T1.x = 2;
	      T2.x = 2;
	      T3.x = 1;
	      T4.x = 2;
	      T5.x = 4;
	      T6.x = 1;
	      
	      T.l = T1;
	      T.r = T2;
	      T1.l = T3;
	      T1.r = T4;
	      T2.l = T5;
	      T2.r = T6;
	      
	      Test test = new Test();
	      int output = test.solution(T);
	      System.out.println(output);
	      
	      
	   }
}
