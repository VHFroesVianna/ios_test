# Mobile Developer Evaluation - Dynamic Forms App (iOS)

This README contains specific instructions for the iOS project.

## Summary
This project is a mobile application that dynamically renders forms from JSON files. The iOS version was developed in Swift and uses JSON files (`all-fields.json` and `200-form.json`) to dynamically build the interface.

## Main Features
- Dynamic Form Rendering
- Section-Based Organization
- Local Database Persistence
- Form Management Features (Listing, Creating, and Editing Entries)
- (Optional) Autosave to ensure progress recovery in case of unexpected app closure

## How to Run the Project

1. **Clone the Repository**
   bash
   git clone https://github.com/VHFroesVianna/mobile-dynamic-forms.git
   cd mobile-dynamic-forms/ios

2. **Open in Xcode**
   Open the \`DynamicForms.xcodeproj\` file or the workspace file if using CocoaPods.

3. **Install Dependencies** (Optional, if applicable)  
   If using CocoaPods:
   \`\`\`bash
   pod install
   \`\`\`
   Then, open the generated \`.xcworkspace\` file.

4. **Build and Run**
   Compile the project and run it on the simulator or a physical device configured for development.

## Database
Form data and user-created entries are stored locally. The application loads the JSON content and saves it to the database on first use, ensuring fast and offline access.

## Contact
For questions or suggestions about the project, contact:  
📧 **vhvianna@gmail.com**  
🔗 **GitHub Repository:** [https://github.com/VHFroesVianna](https://github.com/VHFroesVianna)
