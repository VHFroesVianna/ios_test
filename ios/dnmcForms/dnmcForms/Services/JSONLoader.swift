import Foundation
import SwiftyJSON

class JSONLoader {
    static let filename = "all-fields.json"

    // MARK: - Load Sections
    static func loadSections() -> [Section] {
        ensureJSONFileExists()
        let fileURL = getDocumentsDirectory().appendingPathComponent(filename)

        do {
            let data = try Data(contentsOf: fileURL)
            let json = try JSON(data: data)

            let allFields = parseFields(from: json["fields"])
            var sections: [Section] = []
            
            for sectionJSON in json["sections"].arrayValue {
                let fromIndex = sectionJSON["from"].intValue
                let toIndex = sectionJSON["to"].intValue
                
                let sectionFields = Array(allFields[fromIndex...min(toIndex, allFields.count - 1)])
                
                let section = Section(
                    uuid: sectionJSON["uuid"].stringValue,
                    title: sectionJSON["title"].stringValue,
                    from: fromIndex,
                    to: toIndex,
                    index: sectionJSON["index"].intValue,
                    fields: sectionFields
                )
                sections.append(section)
            }
            
            return sections
        } catch {
            print("❌ Error loading JSON: \(error.localizedDescription)")
            return []
        }
    }

    // MARK: - Ensure JSON Exists in Documents Directory
    static func ensureJSONFileExists() {
        let fileManager = FileManager.default
        let fileURL = getDocumentsDirectory().appendingPathComponent(filename)

        if fileManager.fileExists(atPath: fileURL.path) {
            return
        }

        guard let bundleURL = Bundle.main.url(forResource: "all-fields", withExtension: "json") else {
            print("❌ Error: Could not find \(filename) in bundle.")
            return
        }

        do {
            try fileManager.copyItem(at: bundleURL, to: fileURL)
            print("✅ Copied \(filename) to Documents directory.")
        } catch {
            print("❌ Error copying JSON file: \(error.localizedDescription)")
        }
    }

    // MARK: - Parse Fields
    private static func parseFields(from json: JSON) -> [Field] {
        var fields: [Field] = []
        
        for fieldJSON in json.arrayValue {
            let field = Field(
                uuid: fieldJSON["uuid"].stringValue,
                name: fieldJSON["name"].stringValue,
                label: fieldJSON["label"].stringValue,
                type: (FieldType(rawValue: fieldJSON["type"].stringValue) ?? .text).rawValue,
                options: fieldJSON["options"].arrayValue.map { $0["value"].stringValue },
                required: fieldJSON["required"].boolValue
            )
            fields.append(field)
        }
        
        return fields
    }

    // MARK: - Get Documents Directory
    static func getDocumentsDirectory() -> URL {
        FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    }

    // MARK: - Update JSON
    static func updateJSON(newValue: Any, forKey: String) {
        let fileURL = getDocumentsDirectory().appendingPathComponent(filename)

        do {
            let data = try Data(contentsOf: fileURL)
            var json = try JSON(data: data)
            
            json[forKey] = JSON(newValue)
            
            saveJSON(json)
            
            print("✅ JSON updated successfully")
        } catch {
            print("❌ Error updating JSON: \(error.localizedDescription)")
        }
    }

    // MARK: - Add Field
    static func addField(_ field: Field) {
        let fileURL = getDocumentsDirectory().appendingPathComponent(filename)

        do {
            let data = try Data(contentsOf: fileURL)
            var json = try JSON(data: data)
            
            var fieldsArray = json["fields"].arrayValue
            let newFieldJSON: JSON = [
                "uuid": field.uuid,
                "name": field.name,
                "label": field.label,
                "type": field.type,
                "options": field.options ?? ["option_1", "option_2"],
                "required": field.required ?? false
            ]
            fieldsArray.append(newFieldJSON)
            
            json["fields"] = JSON(fieldsArray)
            
            saveJSON(json)
            
            print("✅ New field added successfully")
        } catch {
            print("❌ Error adding new field: \(error.localizedDescription)")
        }
    }

    // MARK: - Remove Field
    static func removeField(withUUID uuid: String) {
        let fileURL = getDocumentsDirectory().appendingPathComponent(filename)

        do {
            let data = try Data(contentsOf: fileURL)
            var json = try JSON(data: data)
            
            var fieldsArray = json["fields"].arrayValue
            fieldsArray.removeAll { $0["uuid"].stringValue == uuid }
            
            json["fields"] = JSON(fieldsArray)
            
            saveJSON(json)
            
            print("✅ Field removed successfully")
        } catch {
            print("❌ Error removing field: \(error.localizedDescription)")
        }
    }

    // MARK: - Save JSON
    private static func saveJSON(_ json: JSON) {
        let fileURL = getDocumentsDirectory().appendingPathComponent(filename)
        
        do {
            let updatedData = try json.rawData()
            try updatedData.write(to: fileURL)
        } catch {
            print("❌ Error saving JSON: \(error.localizedDescription)")
        }
    }
}
