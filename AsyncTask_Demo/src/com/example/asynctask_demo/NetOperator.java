package com.example.asynctask_demo;

public class NetOperator{
	public void operator(){
		try{
			Thread.sleep(500);
		}catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}