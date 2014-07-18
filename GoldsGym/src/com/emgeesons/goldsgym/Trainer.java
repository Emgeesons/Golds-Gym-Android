package com.emgeesons.goldsgym;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class Trainer {

	protected static Map <Integer, String> weightLossMonthlyMap, weightLossMMonthlyMap, weightGainMonthlyMap, weightGainMMonthlyMap; // program
	protected static String[] weeklyBasic1, weeklyBasic2, weeklyIntermediate1, weeklyIntermediate2, weeklyAdvance1, weeklyAdvance2, vacationFunctional1,  weeklyBasic2NC,  weeklyAdvance1NC, functional1, functional2; //weekly
	
	protected static void weightLossMonthlyMap(){
		weightLossMonthlyMap = new HashMap<Integer, String>();
		weightLossMonthlyMap.put(1,"Basic 1");
		weightLossMonthlyMap.put(2,"Basic 2");
		weightLossMonthlyMap.put(3,"Basic 2");
		weightLossMonthlyMap.put(4,"Intermediate 1");
		weightLossMonthlyMap.put(5,"Intermediate 1");
		weightLossMonthlyMap.put(6,"Intermediate 1");
		weightLossMonthlyMap.put(7,"Advance 1");
		weightLossMonthlyMap.put(8,"Advance 1");
		weightLossMonthlyMap.put(9,"Advance 1");
		
	}
	
	protected static void weightLossMMonthlyMap(){
		weightLossMMonthlyMap = new HashMap<Integer, String>();
		weightLossMMonthlyMap.put(1,"Basic 1");
		weightLossMMonthlyMap.put(2,"Basic 2");
		weightLossMMonthlyMap.put(3,"Intermediate 1");
		weightLossMMonthlyMap.put(4,"Intermediate 2");
		weightLossMMonthlyMap.put(5,"Advance 1");
		weightLossMMonthlyMap.put(6,"Advance 2");
		weightLossMMonthlyMap.put(7,"Functional Training 1");
		weightLossMMonthlyMap.put(8,"Functional Training 2");
		weightLossMMonthlyMap.put(0,"Functional Training 2");
		weightLossMMonthlyMap.put(9,"Functional Training 2");

	}
	
	protected static void weightGainMonthlyMap(){
		weightGainMonthlyMap = new HashMap<Integer, String>();
		weightGainMonthlyMap.put(1,"Basic 1");
		weightGainMonthlyMap.put(2,"Basic 2 (NC)");
		weightGainMonthlyMap.put(3,"Intermediate 1");
		weightGainMonthlyMap.put(4,"Intermediate 2");
		weightGainMonthlyMap.put(5,"Advance 1 (NC)");
		weightGainMonthlyMap.put(6,"Advance 2");
		weightGainMonthlyMap.put(7,"Function Training 2");
		weightGainMonthlyMap.put(8,"Function Training 2");
		weightGainMonthlyMap.put(9,"Function Training 2");
	}
	
	protected static void weightGainMMonthlyMap(){
		weightGainMMonthlyMap = new HashMap<Integer, String>();
		weightGainMMonthlyMap.put(1,"Basic 1");
		weightGainMMonthlyMap.put(2,"Basic 2 (NC)");
		weightGainMMonthlyMap.put(3,"Intermediate 1");
		weightGainMMonthlyMap.put(4,"Intermediate 2");
		weightGainMMonthlyMap.put(5,"Advance 1 (NC)");
		weightGainMMonthlyMap.put(6,"Advance 2");
		weightGainMMonthlyMap.put(7,"Function Training 2");
		weightGainMMonthlyMap.put(8,"Function Training 1");
		weightGainMMonthlyMap.put(0,"Function Training 1");
		weightGainMMonthlyMap.put(9,"Function Training 1");
	}

	
	
	//Monday is day 0 and Sunday is Day 6
	protected static void weeklyBasic1(){
		weeklyBasic1 = new String[7];
		weeklyBasic1[0]="Cardio";
		weeklyBasic1[1]="Whole Body Resistance Training";
		weeklyBasic1[2]="Rest";
		weeklyBasic1[3]="Cardio";
		weeklyBasic1[4]="Whole Body Resistance Training";
		weeklyBasic1[5]="Cardio";
		weeklyBasic1[6]="Rest";
		
	}
	
	protected static void weeklyBasic2(){
		weeklyBasic2 = new String[7];
		weeklyBasic2[0]="Lower Body Resistance Training";
		weeklyBasic2[1]="Upper Body Resistance Training";
		weeklyBasic2[2]="Cardio & Abs";
		weeklyBasic2[3]="Rest";
		weeklyBasic2[4]="Lower Body Resistance Training";
		weeklyBasic2[5]="Upper Body Resistance Training";
		weeklyBasic2[6]="Cardio / Rest";
		
	}
	
	protected static void weeklyIntermediate1(){
		weeklyIntermediate1 = new String[7];
		weeklyIntermediate1[0]="Lower Body Resistance Training";
		weeklyIntermediate1[1]="Upper Body Resistance Training (Back + Biceps)";
		weeklyIntermediate1[2]="Cardio & Core (Abs)";
		weeklyIntermediate1[3]="Rest";
		weeklyIntermediate1[4]="Upper Body Resistance Training (Chest + Shoulder + Triceps)";
		weeklyIntermediate1[5]="Cardio & Core (Abs)";
		weeklyIntermediate1[6]="Rest";
		
	}
	
	//There is a catch in this one - week 1 and week 3 have the same schedule. week 2 and week 4 have the same schedule. What do you do about week 5
	protected static void weeklyIntermediate2(){
		weeklyIntermediate2 = new String[14];
		weeklyIntermediate2[0]="Chest, Shoulders & Triceps + Quads & Calves";
		weeklyIntermediate2[1]="Rest";
		weeklyIntermediate2[2]="Back & Biceps + Hamstrings & Abs";
		weeklyIntermediate2[3]="Rest";
		weeklyIntermediate2[4]="Chest, Shoulders & Triceps + Quads & Calves";
		weeklyIntermediate2[5]="Rest";
		weeklyIntermediate2[6]="Rest";
		weeklyIntermediate2[7]="Back & Biceps + Hamstrings & Abs";
		weeklyIntermediate2[8]="Rest";
		weeklyIntermediate2[9]="Chest, Shoulders & Triceps + Quads & Calves";
		weeklyIntermediate2[10]="Rest";
		weeklyIntermediate2[11]="Back & Biceps + Hamstrings & Abs";
		weeklyIntermediate2[12]="Rest";
		weeklyIntermediate2[13]="Rest";
		
	}
	
	
	//Still need to be implemented
	protected static void weeklyAdvance1(){
		weeklyAdvance1 = new String[7];
		weeklyAdvance1[0]="Lower Body Resistance Training (Legs)";
		weeklyAdvance1[1]="Rest";
		weeklyAdvance1[2]="Upper Body Resistance Training (back)";
		weeklyAdvance1[3]="Cardio & Abs/ Core";
		weeklyAdvance1[4]="Upper Body Resistance Training (Chest + Biceps)";
		weeklyAdvance1[5]="Rest";
		weeklyAdvance1[6]="Upper Body Resistance Training (Shoulder + Triceps)";
	}

	protected static void weeklyAdvance2(){
		weeklyAdvance2 = new String[7];
		weeklyAdvance2[0]="Lower Body Resistance Training (Legs)";
		weeklyAdvance2[1]="Rest";
		weeklyAdvance2[2]="Upper Body Resistance Training (back)";
		weeklyAdvance2[3]="Cardio & Abs/ Core";
		weeklyAdvance2[4]="Upper Body Resistance Training (Chest + Biceps)";
		weeklyAdvance2[5]="Rest";
		weeklyAdvance2[6]="Upper Body Resistance Training (Shoulder + Triceps)";
	}
	
	protected static void vacationFunctional1(){
		vacationFunctional1 = new String[7];
		vacationFunctional1[0]="Exercise";
		vacationFunctional1[1]="Rest";
		vacationFunctional1[2]="Exercise";
		vacationFunctional1[3]="Rest";
		vacationFunctional1[4]="Exercise";
		vacationFunctional1[5]="Cardio";
		vacationFunctional1[6]="Rest";
	}
	

	
	protected static void weeklyBasic2NC(){
		weeklyBasic2NC = new String[7];
		weeklyBasic2NC[0]="Lower Body Resistance Training";
		weeklyBasic2NC[1]="Upper Body Resistance Training";
		weeklyBasic2NC[2]="Abs";
		weeklyBasic2NC[3]="Rest";
		weeklyBasic2NC[4]="Lower Body Resistance Training";
		weeklyBasic2NC[5]="Upper Body Resistance Training";
		weeklyBasic2NC[6]="Rest";
	}
	

	
	protected static void weeklyAdvance1NC(){
		weeklyAdvance1NC = new String[7];
		weeklyAdvance1NC[0]="Lower Body Resistance Training (Legs)";
		weeklyAdvance1NC[1]="Rest";
		weeklyAdvance1NC[2]="Upper Body Resistance Training (back)";
		weeklyAdvance1NC[3]="Abs/ Core";
		weeklyAdvance1NC[4]="Upper Body Resistance Training (Chest + Biceps)";
		weeklyAdvance1NC[5]="Rest";
		weeklyAdvance1NC[6]="Upper Body Resistance Training (Shoulder + Triceps)";
	}
	
	protected static void functional1(){
		functional1 = new String[7];
		functional1[0]="Exercise";
		functional1[1]="Rest";
		functional1[2]="Exercise";
		functional1[3]="Rest";
		functional1[4]="Exercise";
		functional1[5]="Cardio";
		functional1[6]="Rest";
	}
	
	protected static void functional2(){
		functional2 = new String[7];
		functional2[0]="Exercise";
		functional2[1]="Rest";
		functional2[2]="Exercise";
		functional2[3]="Rest";
		functional2[4]="Exercise";
		functional2[5]="Cardio";
		functional2[6]="Rest";
	}
}
