package com.example.ooplab3

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    private val trainingMap: MutableMap<String, List<MutableList<Float>>> = mutableMapOf()

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

        accelerationChartManager =
            LineChartManager(binding.lineChartAccelerometer, AVERAGE_WINDOW_LENGTH, acceleration)

        trainChartManager = object :
            LineChartManager(binding.lineChartTrain, AVERAGE_WINDOW_LENGTH, training) {
            override fun onAggregateUpdate(aggregate: FloatArray) {
                super.onAggregateUpdate(aggregate)
                for (i in aggregate.indices) {
                    trainingHistory[i].add(aggregate[i])
                }
            }
        }
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
        binding.switchMode
            .setOnCheckedChangeListener { _, isChecked ->
                mode = if (isChecked) Mode.RECOGNITION else Mode.TRAINING
                binding.textViewMode
                    .setText(if (isChecked) R.string.mode_recognition else R.string.mode_training)
                binding.textViewModeDescription
                    .setText(if (isChecked) R.string.mode_recognition_desc else R.string.mode_training_desc)
            }

        binding.relativeLayoutObscure
            .setOnTouchListener { _, _ ->
                binding.relativeLayoutObscure.visibility == View.VISIBLE
            }

        binding.frameLayoutFeedback
            .setOnTouchListener { _, motionEvent ->
                when (motionEvent.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {

                        binding.switchMode.isEnabled = false
                        when (mode) {
                            Mode.TRAINING -> {
                                for (training in trainingHistory) {
                                    training.clear()
                                }
                                trainChartManager.offset = 0
                                onInitializeData(
                                    training
                                )
                                onFeedbackRecording()
                            }
                            Mode.RECOGNITION -> {
                                for (recognition in recognitionHistory) {
                                    recognition.clear()
                                }
                                recognitionChartManager.offset = 0
                                onInitializeData(
                                    recognition
                                )
                                onFeedbackRecognition()
                            }
                        }
                        isResponsive = true
                    }
                    MotionEvent.ACTION_UP -> {

                        isResponsive = false
                        onHideFeedback()
                        when (mode) {
                            Mode.TRAINING -> {
                                StringInputDialogFragment().apply {
                                    closeCallback = { text ->
                                        trainingMap[text] = trainingHistory
                                    }
                                    show(
                                        supportFragmentManager,
                                        null
                                    )
                                }
                            }
                            Mode.RECOGNITION -> {
                                lifecycleScope.launch {
                                    binding.relativeLayoutObscure.isVisible = true
                                    val dtw = DTW()
                                    var foundLetter = false
                                    Log.d("MyTag", "${trainingMap.size}")
                                    trainingMap.entries.forEach { entry ->
                                        val averages = DoubleArray(3)
                                        for (i in averages.indices) {
                                            val training = entry.value[i].toFloatArray()
                                            val recognition = recognitionHistory[i].toFloatArray()
                                            averages[i] = dtw.compute(recognition, training).distance
                                        }
                                        binding.relativeLayoutObscure.isVisible = false

                                        if(averages.filter { it < 0.02 }.size == 3) {
                                            foundLetter = true
                                            Toast.makeText(
                                                this@MainActivity,
                                                entry.key,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                    if(!foundLetter) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Letter not found",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                        binding.switchMode.isEnabled = true
                    }
                }
                true
            }

        this.onHideFeedback()
    }

    private fun onInitializeData(dataSet: Array<LineDataSet>) {
        for (lineDataSet in dataSet) {
            lineDataSet.clear()
        }
        for (i in 0 until LENGTH_CHART_HISTORY) {
            val entry = Entry(
                i.toFloat(), 0.toFloat()
            )
            for (lineDataSet in dataSet) {
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
            accelerationChartManager.onUpdateChart(sensorEvent.values)

            if (isResponsive) {
                when (mode) {
                    Mode.TRAINING -> {
                        trainChartManager.onUpdateChart(sensorEvent.values)
                    }
                    Mode.RECOGNITION -> {
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

    private fun saveTrainingData() {

    }

    private fun showStringInputDialog() {
        AlertDialog.Builder(this)
            .setMessage("En")
    }
}