/*
* JBoss, Home of Professional Open Source
* Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
* contributors by the @authors tag. See the copyright.txt in the
* distribution for a full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.redhat.acceptance.utils;

public class SafeWait{
		
	  private static boolean For(boolean noExceptions, String errorMessage, int timeoutInSeconds, int intervalInSeconds, ToHappen toHappen) {
	    long start=System.currentTimeMillis();
	    long end=start+(timeoutInSeconds*1000);
	    boolean timeout=false;
	    while(!toHappen.hasHappened() && !timeout){
	      try{
	        Thread.sleep((intervalInSeconds*1000));
	      }catch(InterruptedException ignor){}
//	      System.out.println("[Wait] - waiting... ["+((end-System.currentTimeMillis())/1000)+"s]");
	      timeout=System.currentTimeMillis()>end;
	      if (timeout && !noExceptions){
	        System.out.println("timed out waiting for: "+errorMessage);
//	        throw new RuntimeException("timed out waiting for: "+errorMessage);
	      }
	    }
	    if (timeout) return false;
	    return true;
	  }
	  public static boolean For(String errorMessage, int timeoutInSeconds, ToHappen toHappen) {
	    return For(false, errorMessage, timeoutInSeconds, 1, toHappen);
	  }

    public static boolean For(int timeoutInSeconds, ToHappen toHappen) {
      return For(false, "unspecified wait", timeoutInSeconds, 1, toHappen);
    }
    
}