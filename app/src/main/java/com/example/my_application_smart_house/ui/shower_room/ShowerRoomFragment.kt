package com.example.my_application_smart_house.ui.shower_room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import com.example.my_application_smart_house.R
import com.example.my_application_smart_house.databinding.FragmentShowerRoomBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

class ShowerRoomFragment : Fragment() {

    private val FILE_NAME = "shower_state.txt"

    private lateinit var toggleLight: ToggleButton
    private lateinit var toggleWater: ToggleButton
    private lateinit var toggleVentilation: ToggleButton
    private lateinit var toggleWashingMachine: ToggleButton
    private lateinit var toggleMirrorLight: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shower_room, container, false)

        toggleLight = view.findViewById(R.id.toggleLight)
        toggleWater = view.findViewById(R.id.toggleWater)
        toggleVentilation = view.findViewById(R.id.toggleVentilation)
        toggleWashingMachine = view.findViewById(R.id.toggleWashingMachine)
        toggleMirrorLight = view.findViewById(R.id.toggleMirrorLight)

        loadStateFromFile()

        toggleLight.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(isChecked, toggleWater.isChecked, toggleVentilation.isChecked, toggleWashingMachine.isChecked, toggleMirrorLight.isChecked)
        }

        toggleWater.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, isChecked, toggleVentilation.isChecked, toggleWashingMachine.isChecked, toggleMirrorLight.isChecked)
        }

        toggleVentilation.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWater.isChecked, isChecked, toggleWashingMachine.isChecked, toggleMirrorLight.isChecked)
        }

        toggleWashingMachine.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWater.isChecked, toggleVentilation.isChecked, isChecked, toggleMirrorLight.isChecked)
        }

        toggleMirrorLight.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWater.isChecked, toggleVentilation.isChecked, toggleWashingMachine.isChecked, isChecked)
        }

        return view
    }

    private fun saveStateToFile(light: Boolean, water: Boolean, ventilation: Boolean, washingMachine: Boolean, mirrorLight: Boolean) {
        val state = "$light,$water,$ventilation,$washingMachine,$mirrorLight"
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
                if (parts.size == 5) {
                    toggleLight.isChecked = parts[0].toBoolean()
                    toggleWater.isChecked = parts[1].toBoolean()
                    toggleVentilation.isChecked = parts[2].toBoolean()
                    toggleWashingMachine.isChecked = parts[3].toBoolean()
                    toggleMirrorLight.isChecked = parts[4].toBoolean()
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}