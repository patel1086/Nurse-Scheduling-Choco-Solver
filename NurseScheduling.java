package org.chocosolver.examples.nursescheduling;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class NurseScheduling {
	int period = 30;
	int numNurse = 10;
	Model model = new Model("Nurse Scheduling");
	IntVar[][] roster = model.intVarMatrix("poster", period, numNurse, 0, 3);
	int pattern[] = { 2, 4, 2, 2 }; // {off,Morning,Afternoon,Night}

	// int values[]= {0,1,2,3};
	IntVar[] values = model.intVarArray("values", 4, new int[] { 0, 1, 2, 3 });

	// variable declaration
	String shift[] = { "O", "M", "A", "N" }; // {off,Morning,Afternoon,Night}

	private void show() {
		for (IntVar[] row : roster) {
			for (IntVar box : row) {
				if (box.getValue() == 0)
					System.out.print("O ");
				else if (box.getValue() == 1)
					System.out.print("M ");
				else if (box.getValue() == 2)
					System.out.print("E ");
				else
					System.out.print("N ");

			}
			System.out.println();
		}

	}

	private void solve() {
		for (int i = 0; i < period; i++) {
			IntVar Oocc = model.intVar("Off" + i, pattern[0]);
			IntVar Mocc = model.intVar("Morning" + i, pattern[1]);
			IntVar Eocc = model.intVar("Afternoon" + i, pattern[2]);
			IntVar Nocc = model.intVar("Night" + i, pattern[3]);
			model.count(0, roster[i], Oocc).post();
			model.count(1, roster[i], Mocc).post();
			model.count(2, roster[i], Eocc).post();
			model.count(3, roster[i], Nocc).post();

		}
		// set approximately 1 day off in every 5 days
		for (int i = 0; i < numNurse; i++) {
			IntVar total = model.intVar("shift" + (i + 1), 5, 8);
			System.out.println("Total is " + total.getValue());
			total.eq(roster[0][i].add(roster[1][i], roster[2][i], roster[3][i], roster[4][i])).post();
			total.eq(roster[5][i].add(roster[6][i], roster[7][i], roster[8][i], roster[9][i])).post();
			total.eq(roster[10][i].add(roster[11][i], roster[12][i], roster[13][i], roster[14][i])).post();
			total.eq(roster[15][i].add(roster[16][i], roster[17][i], roster[18][i], roster[19][i])).post();
			total.eq(roster[20][i].add(roster[21][i], roster[22][i], roster[23][i], roster[24][i])).post();
			total.eq(roster[25][i].add(roster[26][i], roster[27][i], roster[28][i], roster[29][i])).post();

			// make each nurse has shift change
			for (int j = 0; j < period - 1; j += 2) {
				model.allDifferent(roster[j][i], roster[j + 1][i]).post();
				;
			}
		}
		boolean isSolve = model.getSolver().solve();
		if (isSolve) {
			show();
		} else {
			System.out.println("No Solution Found");
		}
	}

	public static void main(String[] args) {
		NurseScheduling NS = new NurseScheduling();
		NS.show();
		System.out.println("---------Final Solution----------");
		NS.solve();

	}
}
