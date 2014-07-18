package com.emgeesons.goldsgym;

import java.util.Map;

class Exercises {

	static Map <String, int[]> exercises;
	
	//These match entries of trainer_daily_schedule/xml
	protected static void initializeExercises(){
		exercises= new DefaultHashMap<String, int[]>(new int[] {R.drawable.welcome_bg, R.string.default_exercise});
		exercises.put("Stationary Bike", new int[] {R.drawable.c_1, R.string.stationary_bike});
		exercises.put("Walking Workout", new int[] {R.drawable.c_2, R.string.walking_workout});
		exercises.put("Elliptical Workout", new int[] {R.drawable.c_3, R.string.elliptical_workout});
		exercises.put("Squats", new int[] {R.drawable.s_1, R.string.squats});
		exercises.put("Leg Press", new int[] {R.drawable.s_2, R.string.leg_press});
		exercises.put("Leg Extension", new int[] {R.drawable.s_3, R.string.leg_extension});
		exercises.put("Lunges", new int[] {R.drawable.s_4, R.string.lunges});
		exercises.put("Step Ups", new int[] {R.drawable.s_5, R.string.step_ups});
		exercises.put("Jump Squats", new int[] {R.drawable.s_6, R.string.jump_squats});
		exercises.put("Dynamic Lunges", new int[] {R.drawable.s_7, R.string.dynamic_lunges});
		exercises.put("Standing Calf Raises", new int[] {R.drawable.s_8, R.string.standing_calf_raises});
		exercises.put("Seated Calf Raises", new int[] {R.drawable.s_9, R.string.seated_calf_raises});
		exercises.put("Prone Leg Curl", new int[] {R.drawable.s_10, R.string.prone_leg_curl});
		exercises.put("Seated Leg Curl", new int[] {R.drawable.s_11, R.string.seated_leg_curl});
		exercises.put("Back Extension", new int[] {R.drawable.s_12, R.string.back_extension});
		exercises.put("Lat Pull Down", new int[] {R.drawable.s_13, R.string.lat_pull_down});
		exercises.put("Seated Row", new int[] {R.drawable.s_14, R.string.seated_row});
		exercises.put("Cable Seated Row", new int[] {R.drawable.s_15, R.string.cable_seated_row});
		exercises.put("Bent Over Barbell Row", new int[] {R.drawable.s_16, R.string.bent_over_barbell_row});
		exercises.put("One Arm Dumbbell Row", new int[] {R.drawable.s_17, R.string.one_arm_dumbbell_row});
		exercises.put("Chest Press", new int[] {R.drawable.s_18, R.string.chest_press});
		exercises.put("Chest-Pec Fly", new int[] {R.drawable.s_19, R.string.chest_pec_fly});
		exercises.put("Dumbbell Shrugs", new int[] {R.drawable.s_20, R.string.dumbbell_shrugs});
		exercises.put("Barbell Shrugs", new int[] {R.drawable.s_21, R.string.barbell_shrugs});
		exercises.put("Reverse Pec Fly", new int[] {R.drawable.s_22, R.string.reverse_pec_fly});
		exercises.put("Dumbbell Hammer Curls", new int[] {R.drawable.s_23, R.string.dumbbell_hammer_curls});
		exercises.put("Reverse Cable Curls", new int[] {R.drawable.s_24, R.string.reverse_cable_curls});
		exercises.put("Front Raises", new int[] {R.drawable.s_25, R.string.front_raises});
		exercises.put("Overhead Press", new int[] {R.drawable.s_26, R.string.overhead_press});
		exercises.put("Barbell Overhead Press", new int[] {R.drawable.s_27, R.string.barbbell_overhead_press});
		exercises.put("Lateral Raises", new int[] {R.drawable.s_28, R.string.lateral_raises});
		exercises.put("Bent Over Lateral Raises", new int[] {R.drawable.s_29, R.string.bent_over_lateral_raises});
		exercises.put("Rear Delt Fly", new int[] {R.drawable.s_30, R.string.rear_delt_fly});
		exercises.put("Dumbbell Arm Curl", new int[] {R.drawable.s_31, R.string.dumbbell_arm_curl});
		exercises.put("Barbell Biceps Curl", new int[] {R.drawable.s_32, R.string.barbell_biceps_curl});
		exercises.put("Supination Curl", new int[] {R.drawable.s_33, R.string.supination_curl});
		exercises.put("Preacher Curls", new int[] {R.drawable.s_34, R.string.preacher_curls});
		exercises.put("Reverse Cable Curls", new int[] {R.drawable.s_35, R.string.reverse_cable_curls});
		exercises.put("Cable Triceps Extension", new int[] {R.drawable.s_36, R.string.cable_triceps_extension});
		exercises.put("Dumbbell Tricep Kickback", new int[] {R.drawable.s_37, R.string.dumbbell_tricep_kickback});
		exercises.put("Tricep Cable Pushdown", new int[] {R.drawable.s_38, R.string.tricep_cable_pushdown});
		exercises.put("Cable Kickbacks", new int[] {R.drawable.s_39, R.string.cable_kickbacks});
		exercises.put("Close Grip Bench Press", new int[] {R.drawable.s_40, R.string.close_grip_bench_press});
		exercises.put("Push Ups", new int[] {R.drawable.s_41, R.string.push_ups});
		exercises.put("Decline Bench Press", new int[] {R.drawable.s_42, R.string.decline_bench_press});
		exercises.put("Flat Bench Chest Press", new int[] {R.drawable.s_43, R.string.flat_bench_chest_press});
		exercises.put("Incline Bench Press", new int[] {R.drawable.s_44, R.string.incline_bench_press});
		exercises.put("Cable Cross Over", new int[] {R.drawable.s_45, R.string.cable_cross_over});
		exercises.put("Pull Ups-Chin Ups", new int[] {R.drawable.s_46, R.string.pull_ups_chin_ups});
		exercises.put("Clean And Press", new int[] {R.drawable.s_47, R.string.clean_and_press});
		exercises.put("Crunches", new int[] {R.drawable.s_48, R.string.crunches});
		exercises.put("Reverse Crunches", new int[] {R.drawable.s_49, R.string.reverse_crunches});
		exercises.put("Oblique Crunches", new int[] {R.drawable.s_50, R.string.oblique_crunches});
		exercises.put("Hanging Oblique Crunches", new int[] {R.drawable.s_51, R.string.hanging_oblique_crunches});
		exercises.put("Plank", new int[] {R.drawable.s_52, R.string.plank});
		exercises.put("Incline Bench Leg Raises", new int[] {R.drawable.s_53, R.string.incline_bench_leg_raises});
		exercises.put("Dumbbell Tricep Extension", new int[] {R.drawable.s_54, R.string.dumbbell_tricep_extension});
		exercises.put("Stiff Leg Dead Lift", new int[] {R.drawable.s_55, R.string.stiff_leg_deadlift});
		exercises.put("Conventional Dead Lift", new int[] {R.drawable.s_56, R.string.conventional_dead_lift});
		exercises.put("Tricep Dips", new int[] {R.drawable.s_57, R.string.tricep_dips});
		exercises.put("Cable Overhead Press", new int[] {R.drawable.s_58, R.string.cable_overhead_press});
		exercises.put("Cable Front Raises", new int[] {R.drawable.s_59, R.string.cable_front_raises});
		exercises.put("Cable Lateral Raises", new int[] {R.drawable.s_60, R.string.cable_lateral_raises});
		exercises.put("Burpees", new int[] {R.drawable.f_1, R.string.burpees});
		exercises.put("Clap Pushups", new int[] {R.drawable.f_2, R.string.clap_pushups});
		exercises.put("Step Up And Squat", new int[] {R.drawable.f_3, R.string.step_up_and_squat});
		exercises.put("Mountain Climber", new int[] {R.drawable.f_4, R.string.mountain_climber});
		exercises.put("Duck Walk", new int[] {R.drawable.f_5, R.string.duck_walk});
		exercises.put("Inchworm Walk", new int[] {R.drawable.f_6, R.string.inchworm_walk});
		exercises.put("Frog Jumps", new int[] {R.drawable.f_7, R.string.frog_jumps});
		exercises.put("Farmers Walk", new int[] {R.drawable.f_8, R.string.farmers_walk});
		exercises.put("Spider Man Walk", new int[] {R.drawable.f_9, R.string.spiderman_walk});
		exercises.put("Pull Ups With Speed", new int[] {R.drawable.f_10, R.string.pull_ups_with_speed});
		exercises.put("Skipping With Speed", new int[] {R.drawable.f_11, R.string.skipping_with_speed});
		exercises.put("Squats With Figure Of 8", new int[] {R.drawable.f_12, R.string.squates_with_figure_of_8});
		exercises.put("Step Ups With Lateral Press", new int[] {R.drawable.f_13, R.string.step_up_and_squat});
		exercises.put("Dead Lift With Shrugs", new int[] {R.drawable.f_14, R.string.dead_lift_with_shrugs});
		exercises.put("Squats With Overhead Press", new int[] {R.drawable.f_15, R.string.squats_with_overhead_press});
		exercises.put("Lunges With Bicep Curl", new int[] {R.drawable.f_16, R.string.lunges_with_bicep_curl});
		exercises.put("Standing Calf Raises With Reverse Curls", new int[] {R.drawable.f_17, R.string.standing_calf_raises_with_reverse_curls});
		exercises.put("Bent Over Barbell Rowing With Dumbbell Kick Backs", new int[] {R.drawable.f_18, R.string.bent_over_barbell_rowing_with_dumbbell_kick_backs});
		exercises.put("Decline Bench Press With Crunches", new int[] {R.drawable.f_19, R.string.decline_bench_press_with_crunches});
		exercises.put("Incline Bench Press With Leg Raises", new int[] {R.drawable.f_20, R.string.incline_bench_press_with_leg_raises});
		exercises.put("Walk / Jog", new int[] {R.drawable.f_21, R.string.walk_jog});
	}
}
