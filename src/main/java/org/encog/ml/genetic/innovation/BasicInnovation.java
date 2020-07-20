/*
 * Encog(tm) Core v3.0 - Java Version
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
package org.encog.ml.genetic.innovation;


/**
 * Provides basic functionality for an innovation.
 */
public class BasicInnovation implements Innovation {
	
	/**
	 * The innovation id.
	 */
	private long innovationID;

	/**
	 * @return The innovation ID.
	 */
	public final long getInnovationID() {
		return innovationID;
	}

	/**
	 * Set the innovation id.
	 * @param theInnovationID The innovation id.
	 */
	public final void setInnovationID(final long theInnovationID) {
		this.innovationID = theInnovationID;
	}

}
