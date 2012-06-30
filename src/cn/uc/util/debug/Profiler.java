/**
 * Tiny.cn.uc.ui.geom.Profiler.java, 2010-11-19
 * 
 * Copyright (c) 2010 UC Mobile, All rights reserved.
 */
package cn.uc.util.debug;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Profiler is a helper class, it help to profile system's performance.
 * 
 * <p>
 * <strong> This class can be removed totally along with its methods' invoking
 * statements by ProGuard when the final release of software do not need any
 * performance profiling, you can put the following script in ProGuard's
 * configuration file: </strong>
 * 
 * <pre>
 * -assumenosideeffects class cn.uc.util.Profiler {
 *   &ltmethods&gt;
 * }
 * #&ltmethods&gt -> < methods >
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Profiler {

	private static final Hashtable PROFILERS = new Hashtable();

	private final Thread belong;

	private long[] stepTimes;
	private long[] stepMems;
	private String[] stepNames;
	private int stepMax;
	private int stepIdx;

	public static Profiler getInstance() {

		Thread c = Thread.currentThread();
		Profiler p = (Profiler) PROFILERS.get(c);

		if (p == null) {

			p = new Profiler(c);
			PROFILERS.put(c, p);
		}

		return p;
	}

	private Profiler(Thread aCurrent) {

		belong = aCurrent;
	}

	public Profiler start() {

		return this.start("PROFILER-" + Thread.currentThread(), 20);
	}

	public Profiler start(String aName, int aMaxStep) {

		Assert.assertSame(belong, Thread.currentThread(), Assert.STATE);

		// initialize
		if (stepTimes == null || stepMax < aMaxStep) {

			stepTimes = new long[aMaxStep];
			stepMems = new long[aMaxStep];
			stepNames = new String[aMaxStep];
		}

		stepIdx = 0;
		stepMax = aMaxStep;

		// remove useless profiler, which belong thread is over
		for (Enumeration e = PROFILERS.elements(), keys = PROFILERS.keys(); e.hasMoreElements();) {

			Profiler p = (Profiler) e.nextElement();
			Object key = keys.nextElement();

			if (!p.belong.isAlive()) {
				PROFILERS.remove(key);
			}
		}

		// record start time and memory
		stepNames[stepIdx] = aName;
		stepTimes[stepIdx] = System.currentTimeMillis();
		stepMems[stepIdx] = Runtime.getRuntime().freeMemory();

		return this;
	}

	public void step(String aName) {

		Assert.assertInRangeV1(0, stepIdx, stepMax - 1, Assert.STATE);

		++stepIdx;// increase

		stepNames[stepIdx] = aName;
		stepMems[stepIdx] = Runtime.getRuntime().freeMemory();
		stepTimes[stepIdx] = System.currentTimeMillis();
	}

	public void end() {

		stepTimes = null;
		stepMems = null;
		stepNames = null;
	}

	/**
	 * The string format:
	 * 
	 * [NAME, TOTAL-TIME(TOTAL-STEP)] (STEP-NAME, STEP-TIME, STEP-MEM)
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer(stepMax * 50);

		long time = 0, mem = 0;

		time = stepTimes[stepIdx] - stepTimes[0];

		sb.append("[").append(stepNames[0]).append("], USED:").append(time).append(
			"ms (").append(stepIdx + 1).append(")] ");

		for (int i = 1; i <= stepIdx; ++i) {

			time = stepTimes[i] - stepTimes[i - 1];
			mem = stepMems[i] - stepMems[i - 1] / 1024;

			sb.append("(").append(stepNames[i]).append(", ").append(time).append(
				"ms, ").append(mem).append("kb) ");
		}

		return sb.toString();
	}
}
