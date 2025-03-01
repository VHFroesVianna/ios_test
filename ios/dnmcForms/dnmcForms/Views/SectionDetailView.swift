//
//  SectionDetailView.swift
//  dnmcForms
//
//  Created by Victor Hugo Froes on 2/25/25.
//


import SwiftUI

struct SectionDetailView: View {
    @State private var section: Section
    @State private var fields: [Field]

    init(section: Section) {
        self._section = State(initialValue: section)
        self._fields = State(initialValue: section.fields)
    }

    var body: some View {
        VStack {
            List(fields) { field in
                FieldView(field: field)
            }
            
            Button("Add Field") {
                navigateToAddField()
            }
            .padding()
        }
        .navigationTitle("Section Details")
    }

    func navigateToAddField() {
        let newField = Field(
            uuid: UUID().uuidString,
            name: "new_field_\(fields.count + 1)",
            label: "New Field",
            type: "text",
            options: nil,
            required: false
        )
        
        fields.append(newField)
        section.to += 1

        let addFieldView = AddFieldView(section: $section, fields: $fields)
        let hostingController = UIHostingController(rootView: addFieldView)
        
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first {
            window.rootViewController?.present(hostingController, animated: true)
        }
    }
}

// MARK: - Preview
#Preview {
    SectionDetailView(section: Section(
        uuid: "1e8562c5-1541-4c6f-aabb-000000000014",
        title: "<h1>Identification</h1><p>This section identifies you.</p>",
        from: 0,
        to: 9,
        index: 0,
        fields: [
            Field(uuid: "1", name: "first_name", label: "First Name", type: "text", options: nil, required: true),
            Field(uuid: "2", name: "last_name", label: "Last Name", type: "text", options: nil, required: true)
        ]
    ))
}
