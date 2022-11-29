package com.example.ooplab3;

public final class DTW {

    public static class Result {
        private final int[][] warpingPath;
        private final double distance;

        public Result(final int[][] warpingPath, final double distance) {
            this.warpingPath = warpingPath;
            this.distance = distance;
        }

        public final int[][] getWarpingPath() { return warpingPath; }

        public final double getDistance() {
            return this.distance;
        }
    }

    public DTW() {
    }

    public Result compute(final float[] samples, final float[] template) {
        if (samples.length == 0 || template.length == 0) {
            return new Result(new int[][]{}, Double.NaN);
        }
        int qualifier = 1;
        // Allocate the Warping Path. (Math.max(N, M) <= K < (N + M).
        final int[][] warpingPath = new int[samples.length + template.length][2];
        // Declare the Local Distances.
        final double[][] localDistances = new double[samples.length][template.length];
        // Declare the Global Distances.
        final double[][] globalDistances = new double[samples.length][template.length];
        // Declare the MinimaBuffer.
        final double[] minimalBuffer = new double[3];
        // Declare iteration variables.
        int i, j;
        for (i = 0; i < samples.length; i++) {
            // Fetch the Sample.
            final float sample = samples[i];
            for (j = 0; j < template.length; j++) {
                // Calculate the Distance between the Sample and the Template for this Index.
                localDistances[i][j] = this.getDistanceBetween(sample, template[j]);
            }
        }

        // Initialize the Global.
        globalDistances[0][0] = localDistances[0][0];

        for (i = 1; i < samples.length; i++) {
            globalDistances[i][0] = localDistances[i][0] + globalDistances[i - 1][0];
        }

        for (j = 1; j < template.length; j++) {
            globalDistances[0][j] = localDistances[0][j] + globalDistances[0][j - 1];
        }

        for (i = 1; i < samples.length; i++) {
            for (j = 1; j < template.length; j++) {
                // Accumulate the path.
                globalDistances[i][j] = (Math.min(Math.min(globalDistances[i - 1][j], globalDistances[i - 1][j - 1]), globalDistances[i][j - 1])) + localDistances[i][j];
            }
        }

        // Update iteration variables.
        i = warpingPath[qualifier - 1][0] = (samples.length - 1);
        j = warpingPath[qualifier - 1][1] = (template.length - 1);

        // Whilst there are samples to process.
        while ((i + j) != 0) {
            // Handle the offset.
            if (i == 0) {
                j -= 1;
            } else if (j == 0) {
                i -= 1;
            } else {
                // Update the contents of the MinimaBuffer.
                minimalBuffer[0] = globalDistances[i - 1][j];
                minimalBuffer[1] = globalDistances[i][j - 1];
                minimalBuffer[2] = globalDistances[i - 1][j - 1];
                // Calculate the Index of the Minimum.
                final int minimumIndex = this.getMinimumIndex(minimalBuffer);
                // Declare booleans.
                final boolean minIs0 = (minimumIndex == 0);
                final boolean minIs1 = (minimumIndex == 1);
                final boolean minIs2 = (minimumIndex == 2);
                // Update the iteration components.
                i -= (minIs0 || minIs2) ? 1 : 0;
                j -= (minIs1 || minIs2) ? 1 : 0;
            }
            // Increment the qualifier.
            qualifier++;
            // Update the Warping Path.
            warpingPath[qualifier - 1][0] = i;
            warpingPath[qualifier - 1][1] = j;
        }

        // Return the Result. (Calculate the Warping Path and the Distance.)
        return new Result(this.reverse(warpingPath, qualifier), ((globalDistances[samples.length - 1][template.length - 1]) / qualifier));
    }

    /**
     * Changes the order of the warping path, in increasing order.
     */
    private int[][] reverse(final int[][] path, final int qualifier) {
        final int[][] resultPath = new int[qualifier][2];
        for (int i = 0; i < qualifier; i++) {
            System.arraycopy(path[qualifier - i - 1], 0, resultPath[i], 0, 2);
        }
        return resultPath;
    }

    /**
     * Computes a distance between two points.
     */
    private double getDistanceBetween(double num1, double num2) {
        // Calculate the square error.
        return (num1 - num2) * (num1 - num2);
    }

    /**
     * Finds the index of the minimum element from the given array.
     */
    private int getMinimumIndex(final double[] array) {
        int index = 0;
        double value = array[0];
        for (int i = 1; i < array.length; i++) {
            // Check whether the current value is smaller.
            final boolean lIsSmaller = array[i] < value;
            // Update the search metrics.
            value = lIsSmaller ? array[i] : value;
            index = lIsSmaller ? i : index;
        }
        return index;
    }

}
