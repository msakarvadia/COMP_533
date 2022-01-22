package comp533.mvc;

import grader.basics.execution.BasicProjectExecution;
import gradingTools.comp533s22.assignment1.S22Assignment1Suite;
import trace.grader.basics.GraderBasicsTraceUtility;
// import util.trace.Tracer;
public class A1Tests {
	public static void main(final String[] args) {
		// if you set this to false, grader steps will not be traced
		GraderBasicsTraceUtility.setTracerShowInfo(true);	
		// if you set this to false, all grader steps will be traced,
		// not just the ones that failed		
		
		GraderBasicsTraceUtility.setBufferTracedMessages(true);
		// Change this number if a test trace gets longer than 600 and is clipped
		
		final int length = 600;
		GraderBasicsTraceUtility.setMaxPrintedTraces(length);
		// Change this number if all traces together are longer than 600
		
		final int time = 2000;
		GraderBasicsTraceUtility.setMaxTraces(time);
		// Change this number if your process times out prematurely
		
		final int timer = 5;
		BasicProjectExecution.setProcessTimeOut(timer);
		// You need to always call such a method
		S22Assignment1Suite.main(args);
	}
}
