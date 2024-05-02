package com.example.my_application_smart_house.ui.kitchen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.example.my_application_smart_house.R
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

class KitchenFragment : Fragment() {
    private val FILE_NAME = "kitchen_state.txt"

    private lateinit var toggleLight: ToggleButton
    private lateinit var toggleWater: ToggleButton
    private lateinit var toggleGas: ToggleButton
    private lateinit var toggleDishwasher: ToggleButton
    private lateinit var toggleExtractorHood: ToggleButton
    private lateinit var toggleTV: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kitchen, container, false)

        toggleLight = view.findViewById(R.id.toggleLight)
        toggleWater = view.findViewById(R.id.toggleWater)
        toggleGas = view.findViewById(R.id.toggleGas)
        toggleDishwasher = view.findViewById(R.id.toggleDishwasher)
        toggleExtractorHood = view.findViewById(R.id.toggleExtractorHood)
        toggleTV = view.findViewById(R.id.toggleTV)

        loadStateFromFile()

        toggleLight.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(isChecked, toggleWater.isChecked, toggleGas.isChecked, toggleDishwasher.isChecked, toggleExtractorHood.isChecked, toggleTV.isChecked)
        }

        toggleWater.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, isChecked, toggleGas.isChecked, toggleDishwasher.isChecked, toggleExtractorHood.isChecked, toggleTV.isChecked)
        }

        toggleGas.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWater.isChecked, isChecked, toggleDishwasher.isChecked, toggleExtractorHood.isChecked, toggleTV.isChecked)
        }

        toggleDishwasher.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWater.isChecked, toggleGas.isChecked, isChecked, toggleExtractorHood.isChecked, toggleTV.isChecked)
        }

        toggleExtractorHood.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWater.isChecked, toggleGas.isChecked, toggleDishwasher.isChecked, isChecked, toggleTV.isChecked)
        }

        toggleTV.setOnCheckedChangeListener { _, isChecked ->
            saveStateToFile(toggleLight.isChecked, toggleWater.isChecked, toggleGas.isChecked, toggleDishwasher.isChecked, toggleExtractorHood.isChecked, isChecked)
        }

        return view
    }

    private fun saveStateToFile(light: Boolean, water: Boolean, gas: Boolean, dishwasher: Boolean, extractorHood: Boolean, tv: Boolean) {
        val state = "$light,$water,$gas,$dishwasher,$extractorHood,$tv"
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
                if (parts.size == 6) {
                    toggleLight.isChecked = parts[0].toBoolean()
                    toggleWater.isChecked = parts[1].toBoolean()
                    toggleGas.isChecked = parts[2].toBoolean()
                    toggleDishwasher.isChecked = parts[3].toBoolean()
                    toggleExtractorHood.isChecked = parts[4].toBoolean()
                    toggleTV.isChecked = parts[5].toBoolean()
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}