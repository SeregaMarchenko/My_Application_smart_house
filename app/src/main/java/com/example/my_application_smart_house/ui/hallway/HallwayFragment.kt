package com.example.my_application_smart_house.ui.hallway

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import com.example.my_application_smart_house.R
import com.example.my_application_smart_house.databinding.FragmentHallwayBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

class HallwayFragment : Fragment() {

    private val FILE_NAME = "hallway_state.txt"

    private lateinit var toggleLight: ToggleButton
    private lateinit var toggleWiFi: ToggleButton
    private lateinit var toggleYandexStation: ToggleButton
    private lateinit var toggleDoorAccess: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hallway, container, false)

        toggleLight = view.findViewById(R.id.toggleLight)
        toggleWiFi = view.findViewById(R.id.toggleWiFi)
        toggleYandexStation = view.findViewById(R.id.toggleYandexStation)
        toggleDoorAccess = view.findViewById(R.id.toggleDoorAccess)

        loadStateFromFile()

        toggleLight.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(isChecked, toggleWiFi.isChecked, toggleYandexStation.isChecked, toggleDoorAccess.isChecked)
        }

        toggleWiFi.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, isChecked, toggleYandexStation.isChecked, toggleDoorAccess.isChecked)
        }

        toggleYandexStation.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWiFi.isChecked, isChecked, toggleDoorAccess.isChecked)
        }

        toggleDoorAccess.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWiFi.isChecked, toggleYandexStation.isChecked, isChecked)
        }

        return view
    }

    private fun saveStateToFile(light: Boolean, wifi: Boolean, yandexStation: Boolean, doorAccess: Boolean) {
        val state = "$light,$wifi,$yandexStation,$doorAccess"
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
                    toggleWiFi.isChecked = parts[1].toBoolean()
                    toggleYandexStation.isChecked = parts[2].toBoolean()
                    toggleDoorAccess.isChecked = parts[3].toBoolean()
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}