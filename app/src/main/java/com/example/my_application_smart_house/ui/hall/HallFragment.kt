package com.example.my_application_smart_house.ui.hall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import com.example.my_application_smart_house.R
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

class HallFragment : Fragment() {

    private val FILE_NAME = "hall_state.txt"

    private lateinit var toggleLight: ToggleButton
    private lateinit var toggleAirFreshener: ToggleButton
    private lateinit var toggleTV: ToggleButton
    private lateinit var toggleSpeakers: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hall, container, false)

        toggleLight = view.findViewById(R.id.toggleLight)
        toggleAirFreshener = view.findViewById(R.id.toggleAirFreshener)
        toggleTV = view.findViewById(R.id.toggleTV)
        toggleSpeakers = view.findViewById(R.id.toggleSpeakers)

        loadStateFromFile()

        toggleLight.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(isChecked, toggleAirFreshener.isChecked, toggleTV.isChecked, toggleSpeakers.isChecked)
        }

        toggleAirFreshener.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, isChecked, toggleTV.isChecked, toggleSpeakers.isChecked)
        }

        toggleTV.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleAirFreshener.isChecked, isChecked, toggleSpeakers.isChecked)
        }

        toggleSpeakers.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleAirFreshener.isChecked, toggleTV.isChecked, isChecked)
        }

        return view
    }

    private fun saveStateToFile(light: Boolean, airFreshener: Boolean, tv: Boolean, speakers: Boolean) {
        val state = "$light,$airFreshener,$tv,$speakers"
        try {
            val file = File(requireContext().filesDir, FILE_NAME)
            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(state)
            bufferedWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadStateFromFile() {
        try {
            val file = File(requireContext().filesDir, FILE_NAME)
            if (!file.exists()) {
                // Создание файла, если его нет
                file.createNewFile()
                return
            }

            val fis = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(fis))
            val line = reader.readLine()
            if (line != null) {
                val parts = line.split(",")
                if (parts.size == 4) {
                    toggleLight.isChecked = parts[0].toBoolean()
                    toggleAirFreshener.isChecked = parts[1].toBoolean()
                    toggleTV.isChecked = parts[2].toBoolean()
                    toggleSpeakers.isChecked = parts[3].toBoolean()
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}