import SwiftUI
import composeApp

@main
struct iosApp: App {
    init() {
        InitKoinKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
