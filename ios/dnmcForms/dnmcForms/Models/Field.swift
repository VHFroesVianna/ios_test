import Foundation

// MARK: - Field Model
struct Field: Identifiable, Codable {
    var id = UUID()
    let uuid: String
    let name: String
    let label: String
    let type: FieldType
    let options: [String]?
    let required: Bool?

    init(uuid: String, name: String, label: String, type: String, options: [String]?, required: Bool?) {
        self.uuid = uuid
        self.name = name
        self.label = label
        self.options = options
        self.required = required
        self.type = FieldType(rawValue: type) ?? .text
    }
}

// MARK: - Field Type Enum
enum FieldType: String, Codable {
    case description
    case dropdown
    case text
    case number
}
