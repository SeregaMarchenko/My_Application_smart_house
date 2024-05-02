package com.example.my_application_smart_house.ui.childrens_room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import com.example.my_application_smart_house.R
import com.example.my_application_smart_house.databinding.FragmentChildrensRoomBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

class ChildrensRoomFragment : Fragment() {

    private val FILE_NAME = "kids_room_state.txt"

    private lateinit var toggleLight: ToggleButton
    private lateinit var toggleVentilation: ToggleButton
    private lateinit var toggleAirFreshener: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_childrens_room, container, false)

        toggleLight = view.findViewById(R.id.toggleLight)
        toggleVentilation = view.findViewById(R.id.toggleVentilation)
        toggleAirFreshener = view.findViewById(R.id.toggleAirFreshener)

        loadStateFromFile()

        toggleLight.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(isChecked, toggleVentilation.isChecked, toggleAirFreshener.isChecked)
        }

        toggleVentilation.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, isChecked, toggleAirFreshener.isChecked)
        }

        toggleAirFreshener.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleVentilation.isChecked, isChecked)
        }

        return view
    }
    private fun saveStateToFile(light: Boolean, ventilation: Boolean, airFreshener: Boolean) {
        val state = "$light,$ventilation,$airFreshener"
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
                if (parts.size == 3) {
                    toggleLight.isChecked = parts[0].toBoolean()
                    toggleVentilation.isChecked = parts[1].toBoolean()
                    toggleAirFreshener.isChecked = parts[2].toBoolean()
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}