package com.example.ooplab3

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import java.util.Arrays

open class LineChartManager(
    private val lineChart: LineChart,
    private val window: Int,
    private val dataSets: Array<LineDataSet>
) {

    var offset: Int = 0

    private val buffer: FloatArray = FloatArray(dataSets.size)

    /** Updates the Chart.  */
    open fun onUpdateChart(vertical: FloatArray) {
        // Increment the Offset.
        ++offset
        // Buffer the Averages.
        for (i in vertical.indices) {
            // Accumulate.
            buffer[i] += vertical[i]
        }
        // Check whether window length is reached.
        if (offset % window == 0) {
            // Perform an aggregated update.
            onAggregateUpdate(buffer)
            // Clear the Buffer.
            Arrays.fill(buffer, 0.0f)
        }
    }

    /** Called when the number of samples displayed on the graph have satisfied the window size.  */
    open fun onAggregateUpdate(aggregate: FloatArray) {
        // Update the chart.
        for (i in dataSets.indices) {
            // Calculate the Average.
            val average = buffer[i] / window
            // Fetch the DataSet.
            val lineDataSet = dataSets[i]
            // Write this Value to the Aggregate for subclasses.
            aggregate[i] = average
            // Remove the oldest element.
            lineDataSet.removeFirst()
            // Buffer the Average.
            lineDataSet.addEntry(Entry(offset.toFloat(), average))
        }
        // Invalidate the Graph. (Ensure it is redrawn!)
        lineChart.invalidate()
    }
}