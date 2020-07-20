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
package org.encog.ml.kmeans;

import org.encog.ml.MLCluster;
import org.encog.ml.MLClustering;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataPair;

/**
 * This class performs a basic K-Means clustering. This class can be used on
 * either supervised or unsupervised data. For supervised data, the ideal values
 * will be ignored.
 * 
 * http://en.wikipedia.org/wiki/Kmeans
 * 
 */
public class KMeansClustering implements MLClustering {

	/**
	 * Calculate the euclidean distance between a centroid and data.
	 * @param c The centroid to use.
	 * @param data The data to use.
	 * @return The distance.
	 */
	public static double calculateEuclideanDistance(final Centroid c,
			final MLData data) {
		final double[] d = data.getData();
		double sum = 0;

		for (int i = 0; i < c.getCenters().length; i++) {
			sum += Math.pow(d[i] - c.getCenters()[i], 2);
		}

		return Math.sqrt(sum);
	}

	/**
	 * The clusters.
	 */
	private final KMeansCluster[] clusters;

	/**
	 * The dataset to cluster.
	 */
	private final MLDataSet set;

	/**
	 * Within-cluster sum of squares (WCSS).
	 */
	private double wcss;

	/**
	 * Construct the K-Means object.
	 * 
	 * @param k
	 *            The number of clusters to use.
	 * @param theSet
	 *            The dataset to cluster.
	 */
	public KMeansClustering(final int k, final MLDataSet theSet) {

		this.clusters = new KMeansCluster[k];
		for (int i = 0; i < k; i++) {
			this.clusters[i] = new KMeansCluster();
		}
		this.set = theSet;

		setInitialCentroids();

		// break up the data over the clusters
		int clusterNumber = 0;

		for (final MLDataPair pair : set) {

			this.clusters[clusterNumber].add(pair.getInput());

			clusterNumber++;

			if (clusterNumber >= this.clusters.length) {
				clusterNumber = 0;
			}
		}

		calcWCSS();

		for (final KMeansCluster element : this.clusters) {
			element.getCentroid().calcCentroid();
		}

		calcWCSS();
	}

	/**
	 * Calculate the within-cluster sum of squares (WCSS).
	 */
	private void calcWCSS() {
		double temp = 0;
		for (final KMeansCluster element : this.clusters) {
			temp = temp + element.getSumSqr();
		}
		this.wcss = temp;
	}

	/**
	 * @return The clusters.
	 */
	@Override
	public final MLCluster[] getClusters() {
		return this.clusters;
	}

	/**
	 * Get the maximum, over all the data, for the specified index.
	 * 
	 * @param index
	 *            An index into the input data.
	 * @return The maximum value.
	 */
	private double getMaxValue(final int index) {
		double result = Double.MIN_VALUE;
		final long count = this.set.getRecordCount();

		for (int i = 0; i < count; i++) {
			final MLDataPair pair = BasicMLDataPair.createPair(
					this.set.getInputSize(), this.set.getIdealSize());
			this.set.getRecord(i, pair);
			result = Math.max(result, pair.getInputArray()[index]);
		}
		return result;
	}

	/**
	 * Get the minimum, over all the data, for the specified index.
	 * 
	 * @param index
	 *            An index into the input data.
	 * @return The minimum value.
	 */
	private double getMinValue(final int index) {
		double result = Double.MAX_VALUE;
		final long count = this.set.getRecordCount();
		final MLDataPair pair = BasicMLDataPair.createPair(
				this.set.getInputSize(), this.set.getIdealSize());

		for (int i = 0; i < count; i++) {
			this.set.getRecord(i, pair);
			result = Math.min(result, pair.getInputArray()[index]);
		}
		return result;
	}

	/**
	 * @return Within-cluster sum of squares (WCSS).
	 */
	public final double getWCSS() {
		return this.wcss;
	}

	/**
	 * Perform a single training iteration.
	 */
	@Override
	public final void iteration() {

		// loop over all clusters
		for (final KMeansCluster element : this.clusters) {
			for (int k = 0; k < element.size(); k++) {

				final MLData data = element.get(k);
				double distance = KMeansClustering.calculateEuclideanDistance(
						element.getCentroid(), data);
				KMeansCluster tempCluster = null;
				boolean match = false;

				for (final KMeansCluster cluster : this.clusters) {

					final double d = KMeansClustering
							.calculateEuclideanDistance(cluster.getCentroid(),
									element.get(k));
					if (distance > d) {
						distance = d;
						tempCluster = cluster;
						match = true;
					}
				}

				if (match) {
					tempCluster.add(element.get(k));
					element.remove(element.get(k));
					for (final KMeansCluster element2 : this.clusters) {
						element2.getCentroid().calcCentroid();
					}
					calcWCSS();
				}
			}
		}
	}

	/**
	 * The number of iterations to perform.
	 * 
	 * @param count
	 *            The count of iterations.
	 */
	@Override
	public final void iteration(final int count) {
		for (int i = 0; i < count; i++) {
			iteration();
		}
	}

	/**
	 * @return The number of clusters.
	 */
	@Override
	public final int numClusters() {
		return this.clusters.length;
	}

	/**
	 * Setup the initial centroids.
	 */
	private void setInitialCentroids() {
		for (int n = 1; n <= this.clusters.length; n++) {

			final double[] temp = new double[this.set.getInputSize()];
			for (int j = 0; j < temp.length; j++) {
				temp[j] = (((getMaxValue(j) 
						- getMinValue(j)) / (this.clusters.length + 1)) * n)
						+ getMinValue(j);
			}
			final Centroid c1 = new Centroid(temp);
			this.clusters[n - 1].setCentroid(c1);
			c1.setCluster(this.clusters[n - 1]);
		}
	}
}
