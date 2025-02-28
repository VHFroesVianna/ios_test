package com.example.dnmcformsandroid.controllers

import android.content.Context
import android.util.Log
import com.example.dnmcformsandroid.models.Field
import com.example.dnmcformsandroid.models.FieldType
import com.example.dnmcformsandroid.models.Section
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.UUID

object JSONLoader {
    fun loadSections(context: Context): List<Section> {
        Log.d("JSONLoader", "Loading JSON file...")

        val jsonString = readJsonFromInternalStorage(context, "all-fields.json")

        return try {
            val sections = parseJson(jsonString)
            Log.d("JSONLoader", "✅ Loaded ${sections.size} sections successfully")
            sections
        } catch (e: Exception) {
            Log.e("JSONLoader", "❌ Error parsing JSON: ${e.localizedMessage}")
            emptyList()
        }
    }

    private fun readJsonFromInternalStorage(context: Context, fileName: String): String {
        val file = File(context.filesDir, fileName)

        return if (file.exists()) {
            file.readText()  // Read from internal storage
        } else {
            context.assets.open(fileName).bufferedReader().use { it.readText() } // Read from assets if missing
        }
    }

    private fun parseJson(jsonString: String): List<Section> {
        val jsonObject = JSONObject(jsonString)

        // ✅ Get all fields first
        val allFieldsArray = jsonObject.optJSONArray("fields") ?: JSONArray()
        val allFields = parseAllFields(allFieldsArray)

        // ✅ Get sections and map their fields
        val sectionsArray = jsonObject.getJSONArray("sections")
        val sections = mutableListOf<Section>()

        for (i in 0 until sectionsArray.length()) {
            val sectionJson = sectionsArray.getJSONObject(i)
            val fromIndex = sectionJson.getInt("from")
            val toIndex = sectionJson.getInt("to")

            // ✅ Extract fields for this section using "from" and "to"
            val sectionFields = allFields.subList(fromIndex, minOf(toIndex + 1, allFields.size))

            val section = Section(
                uuid = sectionJson.getString("uuid"),
                title = sectionJson.getString("title"),
                from = fromIndex,
                to = toIndex,
                index = sectionJson.getInt("index"),
                fields = sectionFields
            )
            Log.d("JSONLoader", "📌 Parsed Section: ${section.title} with ${sectionFields.size} fields")
            sections.add(section)
        }
        return sections
    }

    private fun parseAllFields(fieldsArray: JSONArray): List<Field> {
        val fields = mutableListOf<Field>()

        for (i in 0 until fieldsArray.length()) {
            val fieldJson = fieldsArray.getJSONObject(i)
            val field = Field(
                uuid = fieldJson.getString("uuid"),
                name = fieldJson.getString("name"),
                label = fieldJson.getString("label"),
                type = fieldJson.getString("type"),
                options = fieldJson.optJSONArray("options")?.let { optionsJson ->
                    List(optionsJson.length()) { optionsJson.getString(it) }
                },
                required = try {
                    fieldJson.getBoolean("required")
                } catch (e: Exception) {
                     false
                }
            )
            fields.add(field)
        }
        return fields
    }

    fun saveNewField(context: Context, sectionId: String, name: String, label: String, type: FieldType, required: Boolean) {
        val file = File(context.filesDir, "all-fields.json")

        // Check if the file exists in internal storage; if not, copy it from assets
        if (!file.exists()) {
            context.assets.open("all-fields.json").use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        // Read the file from internal storage
        val jsonString = file.readText()

        val jsonObject = JSONObject(jsonString)

        val sectionsArray = jsonObject.getJSONArray("sections")
        val fieldsArray = jsonObject.optJSONArray("fields") ?: JSONArray()

        val newField = JSONObject().apply {
            put("uuid", UUID.randomUUID().toString())
            put("name", name)
            put("label", label)
            put("type", type.name.lowercase())
            put("required", required)
        }

        fieldsArray.put(newField)

        // Update the section's "to" index
        for (i in 0 until sectionsArray.length()) {
            val section = sectionsArray.getJSONObject(i)
            if (section.getString("uuid") == sectionId) {
                section.put("to", section.getInt("to") + 1)
                break
            }
        }

        jsonObject.put("fields", fieldsArray)

        // Save the updated JSON back to internal storage
        file.writeText(jsonObject.toString())

        Log.d("JSONLoader", "✅ Field added and saved successfully to internal storage!")
    }
}