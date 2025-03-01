import SwiftUI

struct SectionListView: View {
    @State private var sections: [Section] = JSONLoader.loadSections()

    var body: some View {
        NavigationView {
            List(sections) { section in
                NavigationLink(destination: SectionDetailView(section: section)) {
                    VStack(alignment: .leading) {
                        HTMLTextView(html: section.title)
                            .frame(minHeight: 150)
                    }
                    .padding(.vertical, 20)
                }
            }
            .navigationTitle("Sections")
            .onAppear {
                sections = JSONLoader.loadSections()
            }
        }
    }
}

// MARK: - Preview
#Preview {
    SectionListView()
}
