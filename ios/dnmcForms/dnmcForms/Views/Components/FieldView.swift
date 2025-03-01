//
//  FieldView.swift
//  dnmcForms
//
//  Created by Victor Hugo Froes on 2/25/25.
//


import SwiftUI

struct FieldView: View {
    let field: Field
    @State private var textInput: String = ""
    @State private var selectedDropdownOption: String = ""

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(field.label)
                .font(.headline)

            switch field.type {
            case .text, .number:
                TextField("Enter \(field.label)", text: $textInput)
                    .keyboardType(field.type == .number ? .numberPad : .default)
                    .textFieldStyle(RoundedBorderTextFieldStyle())

            case .dropdown:
                Picker(field.label, selection: $selectedDropdownOption) {
                    ForEach(field.options ?? [], id: \.self) { option in
                        Text(option).tag(option)
                    }
                }
                .pickerStyle(MenuPickerStyle())

            case .description:
                HTMLTextView(html: field.label)

            default:
                TextField("Enter \(field.label)", text: $textInput)
            }
        }
        .padding(.vertical, 4)
    }
}

// MARK: - Preview
#Preview {
    FieldView(field: Field(uuid: "1", name: "bio", label: "<h1>About Me</h1><p>Enter your details.</p>", type: "description", options: nil, required: false))
}
