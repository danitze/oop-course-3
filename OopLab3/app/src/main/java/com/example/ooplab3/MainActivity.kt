package com.example.ooplab3

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.ooplab3.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import java.util.Arrays

class MainActivity : AppCompatActivity(), SensorEventListener {
    companion object {
        private open class LineChartManager(
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

        private enum class Mode {
            /** Defines when the app is recording motion data.  */
            TRAINING,
            /** Defines when the app is attempting to recognize motion data.  */
            RECOGNITION
        }

        /** Empty Description. */
        private val DESCRIPTION_NULL: Description = object : Description() {
            init {
                text = ""
            }
        }

        /** Chart Constants. */
        private const val LENGTH_CHART_HISTORY = 64
        private const val AVERAGE_WINDOW_LENGTH = 1
        private const val DELAY_SENSOR = SensorManager.SENSOR_DELAY_FASTEST

        /** Colors an Array of DataSets.  */
        private fun color(lineDataSets: Array<LineDataSet>, color: IntArray) {
            for (i in lineDataSets.indices) {
                color(lineDataSets[i], color[i])
            }

            R.color.colorPrimary
        }

        /** Colors a LineDataSet.  */
        private fun color(lineDataSet: LineDataSet, color: Int) {
            lineDataSet.color = color
            lineDataSet.circleHoleColor = color
            lineDataSet.setCircleColor(color)
        }
    }

    private lateinit var binding: ActivityMainBinding

    /** Member Variables. */
    private var mode: Mode = Mode.TRAINING
    private var isResponsive: Boolean = false
    private val sensorManager: SensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }

    /** Data. */
    private val accelerationData: LineData = LineData()
    private val trainData: LineData = LineData()
    private val recognitionData: LineData = LineData()

    /** Datasets. */
    private val acceleration: Array<LineDataSet> = arrayOf(
        LineDataSet(null, "X"),
        LineDataSet(null, "Y"),
        LineDataSet(null, "Z")
    )
    private val training: Array<LineDataSet> = arrayOf(
        LineDataSet(null, "X"),
        LineDataSet(null, "Y"),
        LineDataSet(null, "Z")
    )
    private val recognition: Array<LineDataSet> = arrayOf(
        LineDataSet(null, "X"),
        LineDataSet(null, "Y"),
        LineDataSet(null, "Z")
    )

    /** Chart Managers. */
    private lateinit var accelerationChartManager: LineChartManager
    private lateinit var trainChartManager: LineChartManager
    private lateinit var recognitionChartManager: LineChartManager

    /** History Lists. */
    private val trainingHistory: List<MutableList<Float>> =
        listOf(mutableListOf(), mutableListOf(), mutableListOf())
    private val recognitionHistory: List<MutableList<Float>> =
        listOf(mutableListOf(), mutableListOf(), mutableListOf())

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Register the Line Data Sources.
        binding.lineChartAccelerometer.data = accelerationData
        binding.lineChartTrain.data = trainData
        binding.lineChartRecognize.data = recognitionData

        // Enable AutoScaling.
        binding.lineChartAccelerometer.isAutoScaleMinMaxEnabled = true
        binding.lineChartTrain.isAutoScaleMinMaxEnabled = true
        binding.lineChartRecognize.isAutoScaleMinMaxEnabled = true

        // Hide the left axis for training and recognition.
        binding.lineChartTrain.axisLeft.setDrawLabels(false)
        binding.lineChartRecognize.axisRight.setDrawLabels(false)

        binding.lineChartAccelerometer.description = DESCRIPTION_NULL
        binding.lineChartTrain.description = DESCRIPTION_NULL
        binding.lineChartRecognize.description = DESCRIPTION_NULL

        // Initialize chart data.
        onInitializeData(acceleration)
        onInitializeData(training)
        onInitializeData(recognition)

        // Register the LineDataSets.
        for (lineDataSet in acceleration) {
            accelerationData.addDataSet(lineDataSet)
        }
        for (lineDataSet in training) {
            trainData.addDataSet(lineDataSet)
        }
        for (lineDataSet in recognition) {
            recognitionData.addDataSet(lineDataSet)
        }

        // Assert that the DataSet has changed, and that we'll be using these three sources.
        accelerationData.notifyDataChanged()
        trainData.notifyDataChanged()
        recognitionData.notifyDataChanged()

        // Color the DataSets.
        color(acceleration, intArrayOf(Color.RED, Color.GREEN, Color.BLUE))
        color(training, intArrayOf(Color.RED, Color.GREEN, Color.BLUE))
        color(recognition, intArrayOf(Color.RED, Color.GREEN, Color.BLUE))

        // Declare the LineChartManager.
        accelerationChartManager =
            LineChartManager(binding.lineChartAccelerometer, AVERAGE_WINDOW_LENGTH, acceleration)

        // Declare the Training and Recognition update handling.
        trainChartManager = object :
            LineChartManager(binding.lineChartTrain, AVERAGE_WINDOW_LENGTH, training) {
            override fun onAggregateUpdate(aggregate: FloatArray) {
                super.onAggregateUpdate(aggregate)
                for (i in aggregate.indices) {
                    trainingHistory[i].add(aggregate[i])
                }
            }
        }
        // Declare Recognition Handling.
        recognitionChartManager = object : LineChartManager(
            binding.lineChartRecognize,
            AVERAGE_WINDOW_LENGTH,
            recognition
        ) {
            override fun onAggregateUpdate(aggregate: FloatArray) {
                super.onAggregateUpdate(aggregate)
                for (i in aggregate.indices) {
                    recognitionHistory[i].add(aggregate[i])
                }

            }
        }
        // Listen for clicks on the Mode switch.
        binding.switchMode
            .setOnCheckedChangeListener { _, isChecked -> // Update the training state.
                mode = if (isChecked) Mode.RECOGNITION else Mode.TRAINING
                binding.textViewMode
                    .setText(if (isChecked) R.string.mode_recognition else R.string.mode_training)
                binding.textViewModeDescription
                    .setText(if (isChecked) R.string.mode_recognition_desc else R.string.mode_training_desc)
            }

        // Handle the ObscureLayout.
        binding.relativeLayoutObscure
            .setOnTouchListener { _, _ ->
                binding.relativeLayoutObscure.visibility == View.VISIBLE
            }

        // Listen for Touch Events on the FeedbackLayout.
        binding.frameLayoutFeedback
            .setOnTouchListener { _, motionEvent -> // Handle the MotionEvent.
                when (motionEvent.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {

                        binding.switchMode.isEnabled = false
                        when (mode) {
                            Mode.TRAINING -> {
                                // Reset the Training History.
                                for (training in trainingHistory) {
                                    training.clear()
                                }
                                // Reset the Training Chart.
                                trainChartManager.offset = 0
                                // Re-initialize the Training Data.
                                onInitializeData(
                                    training
                                )
                                // Assert that we're recording.
                                onFeedbackRecording()
                            }
                            Mode.RECOGNITION -> {
                                // Reset the Recognition History.
                                for (recognition in recognitionHistory) {
                                    recognition.clear()
                                }
                                // Reset the Recognition Chart.
                                recognitionChartManager.offset = 0
                                // Re-initialize the Recognition Data.
                                onInitializeData(
                                    recognition
                                )
                                // Assert that we're listening.
                                onFeedbackRecognition()
                            }
                        }
                        // Assert that we're now responsive.
                        isResponsive = true
                    }
                    MotionEvent.ACTION_UP -> {

                        // We're no longer responsive.
                        isResponsive = false
                        // Hide the FeedbackLayout.
                        onHideFeedback()
                        when (mode) {
                            Mode.TRAINING -> {}
                            Mode.RECOGNITION -> {
                                lifecycleScope.launch {
                                    binding.relativeLayoutObscure.isVisible = true
                                    // Declare the Averages.
                                    val averages = DoubleArray(3)
                                    // Declare the Dynamic Time Warping Algorithm.
                                    val dtw = DTW()
                                    for (i in averages.indices) {
                                        // Fetch the Primitive Histories for this Axis.
                                        val training = trainingHistory[i].toFloatArray()
                                        val recognition = recognitionHistory[i].toFloatArray()
                                        // Calculate the distance using Dynamic Time Warping.
                                        averages[i] = dtw.compute(recognition, training).distance
                                    }
                                    binding.relativeLayoutObscure.isVisible = false
                                    Toast.makeText(
                                        this@MainActivity,
                                        "D(X:" + averages[0] + ", Y:" + averages[1] + ", Z:" + averages[2] + ")",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        // Re-enable the Switch.
                        binding.switchMode.isEnabled = true
                    }
                }
                true
            }
        // Hide the Feedback Layout.
        this.onHideFeedback()
    }

    private fun onInitializeData(dataSet: Array<LineDataSet>) {
        // Ensure the DataSets are empty.
        for (lineDataSet in dataSet) {
            lineDataSet.clear()
        }
        // Initialize the Acceleration Charts.
        for (i in 0 until LENGTH_CHART_HISTORY) {
            // Allocate a the default Entry.
            val entry = Entry(
                i.toFloat(), 0.toFloat()
            )
            for (lineDataSet in dataSet) {
                // Buffer the Entry.
                lineDataSet.addEntry(entry)
            }
        }
    }

    /** Hides the Feedback View.  */
    private fun onHideFeedback() {
        binding.imageViewFeedback.isVisible = false
        binding.frameLayoutFeedback
            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent))
    }

    /** Asserts that we're recording.  */
    private fun onFeedbackRecording() {
        binding.imageViewFeedback.isVisible = true
        binding.imageViewFeedback.setImageResource(R.drawable.ic_voicemail_black_24dp)
        binding.frameLayoutFeedback
            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorMature))
    }

    /** Asserts that we're recognizing.  */
    private fun onFeedbackRecognition() {
        binding.imageViewFeedback.isVisible = true
        binding.imageViewFeedback.setImageResource(R.drawable.ic_gesture_black_24dp)
        binding.frameLayoutFeedback
            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorMature))
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // Update the LineChartManager.
            accelerationChartManager.onUpdateChart(sensorEvent.values)

            if (isResponsive) {
                when (mode) {
                    Mode.TRAINING -> {
                        // Update the Training Chart.
                        trainChartManager.onUpdateChart(sensorEvent.values)
                    }
                    Mode.RECOGNITION -> {
                        // Update the Recognition Chart.
                        recognitionChartManager.onUpdateChart(sensorEvent.values)
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            DELAY_SENSOR
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}