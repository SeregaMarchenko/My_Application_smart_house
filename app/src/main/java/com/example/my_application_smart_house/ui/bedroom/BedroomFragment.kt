package com.example.my_application_smart_house.ui.bedroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import com.example.my_application_smart_house.R
import com.example.my_application_smart_house.databinding.FragmentBedroomBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

class BedroomFragment : Fragment() {

    private val FILE_NAME = "bedroom_state.txt"

    private lateinit var toggleLight: ToggleButton
    private lateinit var toggleTV: ToggleButton
    private lateinit var toggleAirFreshener: ToggleButton
    private lateinit var toggleVacuumRobot: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bedroom, container, false)

        toggleLight = view.findViewById(R.id.toggleLight)
        toggleTV = view.findViewById(R.id.toggleTV)
        toggleAirFreshener = view.findViewById(R.id.toggleAirFreshener)
        toggleVacuumRobot = view.findViewById(R.id.toggleVacuumRobot)

        loadStateFromFile()

        toggleLight.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(isChecked, toggleTV.isChecked, toggleAirFreshener.isChecked, toggleVacuumRobot.isChecked)
        }

        toggleTV.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, isChecked, toggleAirFreshener.isChecked, toggleVacuumRobot.isChecked)
        }

        toggleAirFreshener.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleTV.isChecked, isChecked, toggleVacuumRobot.isChecked)
        }

        toggleVacuumRobot.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleTV.isChecked, toggleAirFreshener.isChecked, isChecked)
        }

        return view
    }

    private fun saveStateToFile(light: Boolean, tv: Boolean, airFreshener: Boolean, vacuumRobot: Boolean) {
        val state = "$light,$tv,$airFreshener,$vacuumRobot"
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
                    toggleTV.isChecked = parts[1].toBoolean()
                    toggleAirFreshener.isChecked = parts[2].toBoolean()
                    toggleVacuumRobot.isChecked = parts[3].toBoolean()
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}