/*
 * Encog(tm) Core Unit Tests v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.neural.data;

import java.awt.Graphics;

import junit.framework.TestCase;

import org.encog.ml.data.MLDataError;

public class TestNeuralDataError extends TestCase {
	public void testNeuralDataError()
	{
		try
		{
			int i=0;
			if(i==0)
				throw new MLDataError("test");
			TestCase.assertTrue(false);
		}
		catch(MLDataError e)
		{
		}
		
		try
		{
			Graphics g = null;
			g.create();
		}
		catch(NullPointerException e)
		{
			try
			{
				int i=0;
				if(i==0)
					throw new MLDataError(e);
				TestCase.assertTrue(false);
			}
			catch(MLDataError e2)
			{
				
			}
		}
	}
}
