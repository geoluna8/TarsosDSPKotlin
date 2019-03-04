package com.code3e.luna080119.tarsosdspkotlin

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var captura = false
    var arr : ArrayList<Float> = ArrayList()
    val colorRojo = "#e60000"
    val colorVerde = "#009933"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

        val pdh = PitchDetectionHandler { res, e ->
            val pitchInHz = res.pitch
            runOnUiThread { processPitch(pitchInHz, captura) }
        }
        val pitchProcessor = PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh)
        dispatcher.addAudioProcessor(pitchProcessor)

        val audioThread = Thread(dispatcher, "Audio Thread")
        audioThread.start()
        //Hasta aqui es el codigo de captura

        buttonStart.setOnClickListener {
            //audioThread.start() //metodo run no imprime los datos en la vista
            buttonStart.isEnabled = false
            buttonStop.isEnabled = true
            buttonStop.setBackgroundColor(Color.parseColor(colorRojo))
            buttonStart.setBackgroundColor(Color.GRAY)
            captura = true
            println("Inicia Captura")
            Toast.makeText(this, "Capturando...", Toast.LENGTH_SHORT).show()
        }

        buttonStop.setOnClickListener {
            //audioThread.interrupt()
            buttonStart.isEnabled = true
            buttonStop.isEnabled = false
            buttonStop.setBackgroundColor(Color.GRAY)
            buttonStart.setBackgroundColor(Color.parseColor(colorVerde))
            captura = false
            println("Detiene Captura")
            Toast.makeText(this, "Captura Detenida", Toast.LENGTH_SHORT).show()
            try {
                if (!audioThread.isInterrupted) {
                    audioThread.interrupt()
                }
            } catch (e : Exception){
                println(e)
            }
        } //Investigar y probar mas adelante el comportamiento de TARSOS cuando cambias entre activitys

        verCaptura.setOnClickListener {
            println(arr)
            Toast.makeText(this, "Captura mostrada en consola", Toast.LENGTH_SHORT).show()
        }

    } //termina onCreate


    fun processPitch(pitchInHz: Float, captura: Boolean) {

        pitchText.setText("" + pitchInHz + " Hz")
        //println(pitchInHz)
        //println(captura)

        if(captura){
            arr.add(pitchInHz)
        }

        if (pitchInHz >= 110 && pitchInHz < 123.47) {
            //A
            noteText.setText("A")
            //println("A")
        } else if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
            //B
            noteText.setText("B")
            //println("B")
        } else if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
            //C
            noteText.setText("C")
            //println("C")
        } else if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
            //D
            noteText.setText("D")
            //println("D")
        } else if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            //E
            noteText.setText("E")
            //println("E")
        } else if (pitchInHz >= 174.61 && pitchInHz < 185) {
            //F
            noteText.setText("F")
            //println("F")
        } else if (pitchInHz >= 185 && pitchInHz < 196) {
            //G
            noteText.setText("G")
            //println("G")
        }
    } //termina processPitch





} //termina Clase
