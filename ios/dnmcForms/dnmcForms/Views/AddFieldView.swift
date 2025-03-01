//
//  AddFieldView.swift
//  dnmcForms
//
//  Created by Victor Hugo Froes on 2/26/25.
//


import SwiftUI
import SwiftyJSON

struct AddFieldView: View {
    @Binding var section: Section
    @Binding var fields: [Field]
    
    @State private var fieldType: FieldType = .text
    @State private var fieldName: String = ""
    @State private var fieldLabel: String = ""
    @State private var isRequired: Bool = false

    var body: some View {
        VStack {
            Text("Add New Field")
                .font(.largeTitle)
                .padding()

            TextField("Field Name", text: $fieldName)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()

            TextField("Field Label", text: $fieldLabel)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()

            Picker("Field Type", selection: $fieldType) {
                Text("Text").tag(FieldType.text)
                Text("Number").tag(FieldType.number)
                Text("Dropdown").tag(FieldType.dropdown)
                Text("Description").tag(FieldType.description)
            }
            .pickerStyle(SegmentedPickerStyle())
            .padding()

            Toggle("Required", isOn: $isRequired)
                .padding()

            Button("Save Field") {
                saveField()
            }
            .padding()
        }
    }

    func saveField() {
        guard !fieldName.isEmpty, !fieldLabel.isEmpty else {
            print("❌ Error: Field Name and Label are required.")
            return
        }

        let newField = Field(
            uuid: UUID().uuidString,
            name: fieldName,
            label: fieldLabel,
            type: fieldType.rawValue,
            options: fieldType == .dropdown ? ["Option 1", "Option 2"] : nil,
            required: isRequired
        )

        fields.append(newField)
        section.to += 1

        saveToJSON(newField)
    }

    func saveToJSON(_ field: Field) {
        let fileURL = JSONLoader.getDocumentsDirectory().appendingPathComponent("all-fields.json")

        do {
            let data = try Data(contentsOf: fileURL)
            var json = try JSON(data: data)

            var updatedFields = json["fields"].arrayValue
            let fieldJSON: [String: Any] = [
                "uuid": field.uuid,
                "name": field.name,
                "label": field.label,
                "type": field.type.rawValue,
                "required": field.required ?? false,
                "options": field.options?.map { ["value": $0] } ?? []
            ]
            
            updatedFields.append(JSON(fieldJSON))
            json["fields"] = JSON(updatedFields)

            try json.rawData().write(to: fileURL)
            print("✅ Field saved successfully to \(fileURL.path)!")
        } catch {
            print("❌ Error saving field: \(error.localizedDescription)")
        }
    }

    func getDocumentsDirectory() -> URL {
        FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    }
}

// MARK: - Preview
#Preview {
    AddFieldView(section: .constant(Section(
        uuid: "test-uuid",
        title: "<h1>Test Section</h1>",
        from: 0,
        to: 0,
        index: 0,
        fields: []
    )), fields: .constant([]))
}
